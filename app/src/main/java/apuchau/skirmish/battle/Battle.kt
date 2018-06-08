package apuchau.skirmish.battle

import apuchau.kotlin.InvalidState
import apuchau.skirmish.army.Army
import apuchau.skirmish.battle.fight.AttackResult
import apuchau.skirmish.battle.log.BattleLog
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus
import com.natpryce.Err
import com.natpryce.Ok
import com.natpryce.Result

class Battle private constructor(private val battlefield: Battlefield,
											private val armies: Set<Army>,
											private var soldiersStatuses: SoldiersStatuses,
											private val soldiersPositions: SoldiersBattlePositions,
											private var soldiersActions: SoldiersBattleActions,
											private var battleLog: BattleLog) {

	companion object Factory {

		fun instance(battlefield: Battlefield,
						 armies: Set<Army>,
						 soldiersPositions: SoldiersBattlePositions) : Result<Battle,String> {

			val allSoldiers = armies.map { army -> army.soldiers }.flatten()

			return when {
				!areThereEnoughArmies(armies) -> Err("Not enough armies to battle")
				!doAllArmiesHaveRepresentationInBattlefield(armies, soldiersPositions) -> Err("Not all armies are represented in the battlefield")
				!doAllSoldiersInBattlefieldBelongsToArmies(armies, soldiersPositions) -> Err("Not all soldiers in the battlefield belong to armies")
				!areAllPositionsAreInBattlefieldBounds(battlefield, soldiersPositions) -> Err("Some positions are out of the battlefield bounds")
				else -> Ok(Battle(
					battlefield,
					armies,
					SoldiersStatuses.withAllHealthy(allSoldiers),
					soldiersPositions,
					SoldiersBattleActions.withAllDoingNothing(allSoldiers),
					battleLogForBattleStart(soldiersPositions)))
			}
		}

		private fun areThereEnoughArmies(armies: Set<Army>) = (armies.size >= 2)


		private fun doAllArmiesHaveRepresentationInBattlefield(
			armies: Set<Army>,
			soldiersPositions: SoldiersBattlePositions) =

			armies.all { it.soldiers.any{ soldiersPositions.containsSoldier(it) } }


		private fun doAllSoldiersInBattlefieldBelongsToArmies(
			armies: Set<Army>,
			soldiersPositions: SoldiersBattlePositions) =

			soldiersPositions.soldiers().all{ soldier -> armies.any{ it.containsSoldier(soldier) } }


		private fun areAllPositionsAreInBattlefieldBounds(
			battlefield: Battlefield,
			soldiersPositions: SoldiersBattlePositions) =

			soldiersPositions.areAllWithinBounds(battlefield.boundaries)

		private fun battleLogForBattleStart(soldiersPositions: SoldiersBattlePositions): BattleLog =
			BattleLog.withEntries( soldiersPositions.map { logEntryForStartingPosition(it) } )

		private fun logEntryForStartingPosition(soldierBattlePosition: Pair<Soldier, BattlefieldPosition>): String {
			return "Soldier ${soldierBattlePosition.first.soldierId.uniqueName} starts at position (${soldierBattlePosition.second.x}, ${soldierBattlePosition.second.y})"
		}

	}

	fun snapshot(): BattleSnapshot {
		return BattleSnapshot(battlefield, soldiersStatuses, soldiersPositions, soldiersActions, battleLog)
	}

	override fun toString(): String {
		return "Battle. Battlefield: $battlefield, Armies: $armies, Positions: $soldiersPositions, Statuses: ${soldiersStatuses}"
	}

	private fun armyOf(soldier: Soldier): Army =
		armies.find { army -> army.containsSoldier(soldier) }!!


	fun timeCycle() {
		reevaluateSoldiersActions()
		resolveFighting()
	}

	private fun reevaluateSoldiersActions() {

		val actionsCalculationResult =
			allLiveSoldiers()
			.map { soldier -> newSoldierAction(soldier) }
			.toList()

		actionsCalculationResult.forEach {
			soldiersActions = soldiersActions.byChangingSoldierAction(it.soldier, it.newAction)
		}

		battleLog = battleLog.byAddingEntries(calculateLogEntries(actionsCalculationResult))
	}

	private fun newSoldierAction(soldier: Soldier) =
		ActionCalculationResult(soldier, currentActionForSoldier(soldier), calculateSoldierAction(soldier))

	private fun allLiveSoldiers(): List<Soldier> {
		return armies
			.map { army -> army.soldiers }
			.flatten()
			.filter { soldier -> isAlive(soldier) }
	}

	private fun calculateLogEntries(actionsResult: Collection<ActionCalculationResult>): List<String> =

		actionsResult.map { calculateLogEntry(it) }
			.filterNotNull()
			.toList()

	private fun currentActionForSoldier(soldier: Soldier): SoldierAction =

		soldiersActions.actionForSoldier(soldier) ?: throw InvalidState("Soldier '${soldier}' doesn't have a current action")

	private fun calculateLogEntry(actionResult: ActionCalculationResult): String? =
		when {
			(actionResult.currentAction == SoldierAction.DO_NOTHING
				&& actionResult.newAction == SoldierAction.FIGHT) -> "${actionResult.soldier.soldierId.uniqueName} started fighting"

			else -> null
		}

	private fun calculateSoldierAction(soldier: Soldier): SoldierAction {
		return if (hasAdjacentEnemiesAlive(soldier)) {
			SoldierAction.FIGHT
		}
		else {
			SoldierAction.DO_NOTHING
		}
	}

	private fun hasAdjacentEnemiesAlive(soldier: Soldier): Boolean =
		enemiesAdjacentTo(soldier)
			.filter { adjacentSoldier -> isAlive(adjacentSoldier) }
			.isNotEmpty()

	private fun enemiesAdjacentTo(soldier: Soldier): Set<Soldier> =
		soldiersPositions.soldiersAdjacentToSoldier(soldier)
			.filter { adjacentSoldier -> areSoldierEnemies(soldier, adjacentSoldier) }
			.toSet()

	private fun areSoldierEnemies(soldierA: Soldier, soldierB: Soldier): Boolean =
		armyOf(soldierA) != armyOf(soldierB)

	private fun resolveFighting() {
		soldiersActions.soldiersThatAreFighting()
			.forEach { soldier -> resolveAttacksOf(soldier) }
	}

	private fun resolveAttacksOf(soldier: Soldier) {

		if (isAlive(soldier)) {
			val attackResults = enemiesAdjacentTo(soldier)
				.filter { adjacentSoldier -> isAlive(adjacentSoldier) }
				.map { adjacentSoldier -> resolveAttack(soldier, adjacentSoldier) }

			attackResults.forEach { attachResult ->
				soldiersStatuses = soldiersStatuses.byChangingSoldierStatus(attachResult.defender, attachResult.newDefenderStatus)
			}

			val logEntries = attackResults.map { logEntryForAttack(it) }
			battleLog = battleLog.byAddingEntries(logEntries)
		}
	}

	private fun resolveAttack(attacker: Soldier, defender: Soldier): AttackResult =
		AttackResult(attacker, defender, calculateStatusAfterBeenHit(defender))

	private fun calculateStatusAfterBeenHit(soldier: Soldier): SoldierStatus =
		when(soldierStatus(soldier)) {
			SoldierStatus.HEALTHY -> SoldierStatus.WOUNDED
			SoldierStatus.WOUNDED -> SoldierStatus.DEAD
			SoldierStatus.DEAD -> SoldierStatus.DEAD
		}

	private fun logEntryForAttack(attackResult: AttackResult): String =
		"${attackResult.attacker.soldierId.uniqueName} hit ${attackResult.defender.soldierId.uniqueName} who is now ${attackResult.newDefenderStatus.name}"

	private fun soldierStatus(soldier: Soldier) : SoldierStatus =
		soldiersStatuses.soldierStatus(soldier) ?: throw InvalidState("Found soldier '${soldier} in battle with unknown status")

	private fun isAlive(soldier: Soldier): Boolean = soldierStatus(soldier).isAlive()

}
