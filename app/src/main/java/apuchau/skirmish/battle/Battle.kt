package apuchau.skirmish.battle

import apuchau.kotlin.InvalidState
import apuchau.skirmish.army.Army
import apuchau.skirmish.battle.fight.AttackResult
import apuchau.skirmish.battle.log.BattleLog
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus
import com.natpryce.Err
import com.natpryce.Ok
import com.natpryce.Result

// TODO - Do I need battle class?  Does it have to be mutable / store data of the battle?

class Battle private constructor(private val battlefield: Battlefield,
											private val armies: Set<Army>,
											private var soldiersInBattle: SoldiersInBattle,
											private var battleLog: BattleLog) {

	// TODO - Make soldiersInBattle and battleLog immutable (Battle will became a snapshot, or maybe we create BattleStatus as snapshot (just data),
	// and the Battle can have the fighting engine, etc...)

	companion object Factory {

		fun instance(battlefield: Battlefield,
						 armies: Set<Army>,
						 soldiersInBattle: Collection<SoldierInBattle>): Result<Battle, String> {

			return when {
				!areThereEnoughArmies(armies) -> Err("Not enough armies to battle")
				!doAllArmiesHaveRepresentationInBattlefield(armies, soldiersInBattle) -> Err("Not all armies are represented in the battlefield")
				!doAllSoldiersInBattlefieldBelongToArmies(armies, soldiersInBattle) -> Err("Not all soldiers in the battlefield belong to armies")
				!areAllPositionsInBattlefieldBounds(battlefield, soldiersInBattle) -> Err("Some positions are out of the battlefield bounds")
				areAnySoldiersInSamePosition(soldiersInBattle) -> Err("Some soldiers are in the same position")
				areAnySoldiersDuplicatedInTheBattle(soldiersInBattle) -> Err("Some soldiers are duplicated")
				else -> Ok(
					Battle(
						battlefield,
						armies,
						SoldiersInBattle(soldiersInBattle),
						battleLogForBattleStart(soldiersInBattle)))
			}
		}

		private fun areAnySoldiersInSamePosition(soldiersInBattle: Collection<SoldierInBattle>): Boolean {

			val numDifferentPositions = soldiersInBattle.map { it.position }.distinct().count()
			return numDifferentPositions != soldiersInBattle.count()
		}

		private fun areAnySoldiersDuplicatedInTheBattle(soldiersInBattle: Collection<SoldierInBattle>): Boolean {

			val numDifferentSoldiers = soldiersInBattle.map { it.soldier }.distinct().count()
			return numDifferentSoldiers != soldiersInBattle.count()
		}

		private fun areThereEnoughArmies(armies: Set<Army>) = (armies.size >= 2)


		private fun doAllArmiesHaveRepresentationInBattlefield(armies: Collection<Army>, soldiersInBattle: Collection<SoldierInBattle>) =

			armies.all { army -> army.soldiers.any { armySoldier -> soldiersInBattle.any { it.soldier == armySoldier } } }


		private fun doAllSoldiersInBattlefieldBelongToArmies(
			armies: Set<Army>,
			soldiersInBattle: Collection<SoldierInBattle>) =

			soldiersInBattle.map { it.soldier }.all { soldier -> armies.any { it.containsSoldier(soldier) } }


		private fun areAllPositionsInBattlefieldBounds(battlefield: Battlefield, soldiersInBattle: Collection<SoldierInBattle>) =

			soldiersInBattle.all { battlefield.boundaries.within(it.position) }

		private fun battleLogForBattleStart(soldiersInBattle: Collection<SoldierInBattle>): BattleLog =
			BattleLog.withEntries(soldiersInBattle.map { logEntryForSoldierStartingTheBattle(it) })

		private fun logEntryForSoldierStartingTheBattle(soldierInBattle: SoldierInBattle): String {
			return "Soldier ${soldierInBattle.soldier.soldierId.uniqueName} starts at position (${soldierInBattle.position.x}, ${soldierInBattle.position.y})"
		}

	}

	fun snapshot(): BattleSnapshot = BattleSnapshot(battlefield, soldiersInBattle, battleLog)

	override fun toString(): String {
		return "Battle. Battlefield: $battlefield, Armies: $armies, Status: $soldiersInBattle"
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
				.map { soldier ->
					ActionCalculationResult(
						soldier, currentActionForSoldier(soldier), calculateNewActionForSoldier(soldier))
				}
				.toList()

		val soldiersNewActions = actionsCalculationResult.map {
			Pair(it.soldier, it.newAction)
		}

		soldiersInBattle = soldiersInBattle.applyNewSoldierActions(soldiersNewActions)

		battleLog = battleLog.byAddingEntries(calculateLogEntries(actionsCalculationResult))
	}

	private fun calculateNewActionForSoldier(soldier: Soldier): SoldierAction =
		when {
			hasAdjacentEnemiesAlive(soldier) -> SoldierAction.FIGHT
			else -> SoldierAction.DO_NOTHING
		}

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

	// TODO - Remove the exception, we should be ok with nullable object

	private fun currentActionForSoldier(soldier: Soldier): SoldierAction =

		soldiersInBattle.actionForSoldier(soldier) ?: throw InvalidState("Soldier '${soldier}' doesn't have a current action")

	private fun calculateLogEntry(actionResult: ActionCalculationResult): String? =
		when {
			(actionResult.currentAction == SoldierAction.DO_NOTHING
				&& actionResult.newAction == SoldierAction.FIGHT) -> "${actionResult.soldier.soldierId.uniqueName} started fighting"

			else -> null
		}

	private fun hasAdjacentEnemiesAlive(soldier: Soldier): Boolean =
		enemiesAdjacentTo(soldier)
			.filter { adjacentSoldier -> adjacentSoldier.isAlive() }
			.isNotEmpty()

	private fun enemiesAdjacentTo(soldier: Soldier): Set<SoldierInBattle> =

		soldiersInBattle.soldiersAdjacentToSoldier(soldier)
			.filter { adjacentSoldierInBattle -> areEnemies(soldier, adjacentSoldierInBattle.soldier) }
			.toSet()

	private fun areEnemies(soldierA: Soldier, soldierB: Soldier): Boolean =
		armyOf(soldierA) != armyOf(soldierB)

	private fun resolveFighting() {
		soldiersInBattle = resolveFighting(soldiersInBattle, emptySet())
	}

	private fun resolveFighting(soldiersInBattle: SoldiersInBattle, processedSoldiers: Set<Soldier>) : SoldiersInBattle {

		val soldiersFighting = soldiersInBattle.soldiersFightingInOrderOfBattling()
		return when {
			soldiersFighting.isEmpty() -> soldiersInBattle
			else -> {
				val soldierAttackingMaybe = soldiersFighting.find { it -> ! processedSoldiers.contains(it.soldier) }
				soldierAttackingMaybe?.let { soldierAttacking ->
					val newSoldiersInBattle = resolveSoldierAttacks(soldierAttacking, soldiersInBattle)
					resolveFighting(newSoldiersInBattle, processedSoldiers.union(hashSetOf(soldierAttacking.soldier)))
				} ?:soldiersInBattle
			}
		}
	}

	private fun resolveSoldierAttacks(soldierInBattle: SoldierInBattle, soldiersInBattle: SoldiersInBattle) : SoldiersInBattle =

		when {
			soldierInBattle.isAlive() -> {
				val attackResults =
					enemiesAdjacentTo(soldierInBattle.soldier)
						.filter { it.isAlive() }
						.map { resolveAttack(soldierInBattle, it.soldier) }

				val logEntries= attackResults.map { logEntryForAttack(it) }
				battleLog = battleLog.byAddingEntries(logEntries)

				soldiersInBattle.withSoldiersStatuses(attackResults.map {
					attackResult -> Pair(attackResult.defender, attackResult.newDefenderStatus)
				})
			}
			else -> soldiersInBattle
		}


	private fun resolveAttack(attacker: SoldierInBattle, defender: Soldier): AttackResult =
		AttackResult(attacker.soldier, defender, calculateStatusAfterBeenHit(defender))

	private fun calculateStatusAfterBeenHit(soldier: Soldier): SoldierStatus =
		when(soldierStatus(soldier)) {
			SoldierStatus.HEALTHY -> SoldierStatus.WOUNDED
			SoldierStatus.WOUNDED -> SoldierStatus.DEAD
			SoldierStatus.DEAD -> SoldierStatus.DEAD
		}

	private fun logEntryForAttack(attackResult: AttackResult): String =
		"${attackResult.attacker.soldierId.uniqueName} hit ${attackResult.defender.soldierId.uniqueName} who is now ${attackResult.newDefenderStatus.name}"

	// TODO - Remove exception, we should be ok with nullity
	private fun soldierStatus(soldier: Soldier) : SoldierStatus =
		soldiersInBattle.statusForSoldier(soldier) ?: throw InvalidState("Found soldier '${soldier} in battle with unknown status")

	private fun isAlive(soldier: Soldier): Boolean = soldierStatus(soldier).isAlive()

}
