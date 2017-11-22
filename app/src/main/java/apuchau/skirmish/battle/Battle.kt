package apuchau.skirmish.battle

import apuchau.skirmish.army.Army
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus
import com.natpryce.Err
import com.natpryce.Ok
import com.natpryce.Result

class Battle private constructor(private val battlefield: Battlefield,
											private val armies: Set<Army>,
											private var soldiersStatuses: SoldiersStatuses,
											private val soldiersPositions: SoldiersBattlePositions,
											private var soldiersActions: SoldiersBattleActions) {

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
					SoldiersBattleActions.withAllDoingNothing(allSoldiers)))
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
	}

	fun snapshot(): BattleSnapshot {
		return BattleSnapshot(battlefield, soldiersStatuses, soldiersPositions, soldiersActions)
	}

	override fun toString(): String {
		return "Battle. Battlefield: $battlefield, Armies: $armies, Positions: $soldiersPositions"
	}

	private fun armyOf(soldier: Soldier): Army =
		armies.find { army -> army.containsSoldier(soldier) }!!


	fun timeCycle() {
		putAdjacentSoldiersToFight()
		resolveFighting()
	}

	private fun putAdjacentSoldiersToFight() {

		armies.forEach { army ->
			army.soldiers.forEach {
				soldier ->
					this.soldiersActions = this.soldiersActions.byChangingSoldiersActions(
						listOf(Pair(soldier, calculateSoldierAction(soldier)))
					)
			}
		}
	}

	private fun calculateSoldierAction(soldier: Soldier): SoldierAction {
		return if (hasSoldierAdjacentEnemies(soldier)) {
			SoldierAction.FIGHT
		}
		else {
			SoldierAction.DO_NOTHING
		}
	}

	private fun hasSoldierAdjacentEnemies(soldier: Soldier): Boolean =
		enemiesAdjacentTo(soldier).isNotEmpty()

	private fun enemiesAdjacentTo(soldier: Soldier): Set<Soldier> =
		soldiersPositions.soldiersAdjacentToSoldier(soldier)
			.filter { adjacentSoldier -> areSoldierEnemies(soldier, adjacentSoldier) }
			.toSet()

	private fun areSoldierEnemies(soldierA: Soldier, soldierB: Soldier): Boolean =
		armyOf(soldierA) != armyOf(soldierB)

	private fun resolveFighting() {
		soldiersActions.soldiersThatAreFighting()
			.forEach { soldier -> resolveAttach(soldier) }
	}

	private fun resolveAttach(soldier: Soldier) {
		enemiesAdjacentTo(soldier)
			.forEach {
				adjacentSoldier -> soldiersStatuses =
				soldiersStatuses.byChangingSoldierStatus(adjacentSoldier, calculateStatusAfterBeenHit(adjacentSoldier))}
	}

	private fun calculateStatusAfterBeenHit(soldier: Soldier): SoldierStatus =
		when(soldierStatus(soldier)) {
			SoldierStatus.HEALTHY -> SoldierStatus.WOUNDED
			SoldierStatus.WOUNDED -> SoldierStatus.DEAD
			else -> SoldierStatus.DEAD
		}

	private fun soldierStatus(soldier: Soldier) : SoldierStatus =
		soldiersStatuses.soldierStatus(soldier) ?: SoldierStatus.HEALTHY

}
