package apuchau.skirmish.battle

import apuchau.skirmish.army.Army
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.soldier.Soldier
import com.natpryce.*

class Battle private constructor(val battlefield: Battlefield,
											 private val armies: Set<Army>,
											 private val soldiersPositions: SoldiersBattlePositions) {

	private var soldiersStatuses : Map<Army, Map<Soldier, SoldierStatus>> = emptyMap()

	companion object Factory {

		fun instance(battlefield: Battlefield,
						 armies: Set<Army>,
						 soldiersPositions: SoldiersBattlePositions) : Result<Battle,String> {

			return checkEnoughArmies(armies)
				.flatMap { checkAllArmiesHaveRepresentationInBattlefield(armies, soldiersPositions) }
				.flatMap { checkSoldiersInBattlefieldBelongsToArmies(armies, soldiersPositions) }
				.flatMap { checkPositionsAreInBattlefieldBounds(battlefield, soldiersPositions) }
				.map { Battle(battlefield, armies, soldiersPositions) }
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

	init {
		initSoldiersStatuses(armies)
	}

	private fun initSoldiersStatuses(armies: Set<Army>) {

		this.soldiersStatuses = armies
			.map { army -> Pair(army, army.soldiers.map{ soldier -> Pair(soldier, SoldierStatus.DOING_NOTHING) }.toMap()) }
			.toMap()

	}

	fun snapshot(): BattleSnapshot {
		return BattleSnapshot(battlefield, soldiersPositions, soldiersStatuses)
	}

	override fun toString(): String {
		return "Battle. Battlefield: $battlefield, Armies: $armies, Positions: $soldiersPositions"
	}

	fun timeCycle() {
		putAdjacentSoldiersToFight()
	}

	private fun putAdjacentSoldiersToFight() {

		this.soldiersStatuses = armies
			.map { army -> Pair(army,
				army.soldiers.map{ soldier -> Pair(soldier, calculateSoldierStatus(soldiersPositions, soldier)) }.toMap()) }
			.toMap()

	}

	private fun calculateSoldierStatus(soldiersPositions: SoldiersBattlePositions, soldier: Soldier): SoldierStatus {
		return if (hasSoldierAdjacentEnemies(soldiersPositions, soldier)) {
			SoldierStatus.FIGHTING
		}
		else {
			SoldierStatus.DOING_NOTHING
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
