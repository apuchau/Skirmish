package apuchau.skirmish.battle

import apuchau.skirmish.army.Army
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.soldier.Soldier
import com.natpryce.*

class Battle private constructor(private val battlefield: Battlefield,
											private val armies: Set<Army>,
											private val soldiersPositions: SoldiersBattlePositions,
											private var soldiersActions: SoldiersBattleActions) {

	companion object Factory {

		fun instance(battlefield: Battlefield,
						 armies: Set<Army>,
						 soldiersPositions: SoldiersBattlePositions) : Result<Battle,String> {

			return checkEnoughArmies(armies)
				.flatMap { checkAllArmiesHaveRepresentationInBattlefield(armies, soldiersPositions) }
				.flatMap { checkSoldiersInBattlefieldBelongsToArmies(armies, soldiersPositions) }
				.flatMap { checkPositionsAreInBattlefieldBounds(battlefield, soldiersPositions) }
				.map { Battle(battlefield, armies, soldiersPositions, SoldiersBattleActions.withAllDoingNothing(armies)) }
		}


		private fun checkEnoughArmies(armies: Set<Army>) : Result<Unit,String> {
			return if (armies.size >= 2)
				Ok(Unit)
			else
				Err("You need at least two armies to battle")
		}


		private fun checkAllArmiesHaveRepresentationInBattlefield(
			armies: Set<Army>,
			soldiersPositions: SoldiersBattlePositions)
			: Result<Unit,String> {

			return if (armies.all {
				it.soldiers.any{ soldiersPositions.containsSoldier(it) }
			}) {
				Ok(Unit)
			}
			else {
				Err("Not all armies are represented in the battlefield")
			}
		}

		private fun checkSoldiersInBattlefieldBelongsToArmies(
			armies: Set<Army>,
			soldiersPositions: SoldiersBattlePositions)
			: Result<Unit, String> {

			return if (soldiersPositions.soldiers().all{ soldier -> armies.any{ it.containsSoldier(soldier) } })
				Ok(Unit)
			else
				Err("Not all soldiers in the battlefield belong to armies")
		}

		private fun checkPositionsAreInBattlefieldBounds(
			battlefield: Battlefield,
			soldiersPositions: SoldiersBattlePositions)
			: Result<Unit,String> {

			return if (soldiersPositions.areAllWithinBounds(battlefield.boundaries))
				Ok(Unit)
			else
				Err("Some positions are out of the battlefield bounds")
		}
	}

	fun snapshot(): BattleSnapshot {
		return BattleSnapshot(battlefield, soldiersPositions, soldiersActions)
	}

	override fun toString(): String {
		return "Battle. Battlefield: $battlefield, Armies: $armies, Positions: $soldiersPositions"
	}

	fun timeCycle() {
		putAdjacentSoldiersToFight()
	}

	private fun putAdjacentSoldiersToFight() {

		armies.forEach { army ->
			army.soldiers.forEach {
				soldier ->
					this.soldiersActions = this.soldiersActions.byChangingSoldiersActions(
						listOf(Pair(soldier, calculateSoldierStatus(soldiersPositions, soldier)))
					)
			}
		}
	}

	private fun calculateSoldierStatus(soldiersPositions: SoldiersBattlePositions, soldier: Soldier): SoldierAction {
		return if (hasSoldierAdjacentEnemies(soldiersPositions, soldier)) {
			SoldierAction.FIGHT
		}
		else {
			SoldierAction.DO_NOTHING
		}
	}

	private fun hasSoldierAdjacentEnemies(soldiersPositions: SoldiersBattlePositions, soldier: Soldier): Boolean =
		soldiersPositions.soldiersAdjacentToSoldier(soldier)
			.any { adjacentSoldier -> areSoldierEnemies(soldier, adjacentSoldier) }

	private fun areSoldierEnemies(soldierA: Soldier, soldierB: Soldier): Boolean =
		armyOf(soldierA) != armyOf(soldierB)

	private fun armyOf(soldier: Soldier): Army =
		armies.find { army -> army.containsSoldier(soldier) }!!

}
