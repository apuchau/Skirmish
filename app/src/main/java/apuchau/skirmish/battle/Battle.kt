package apuchau.skirmish.battle

import apuchau.skirmish.army.Army
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.exception.InvalidSoldiersPosition
import apuchau.skirmish.exception.NotEnoughSoldiers
import apuchau.skirmish.exception.SoldierNotInArmy
import apuchau.skirmish.soldier.Soldier

class Battle private constructor(val battlefield: Battlefield,
											 private val armies: Set<Army>,
											 private val soldiersPositions: SoldiersBattlePositions) {

	private var soldiersStatuses : Map<Army, Map<Soldier, SoldierStatus>> = emptyMap()

	companion object Factory {

		fun instance(battlefield: Battlefield,
						 armies: Set<Army>,
						 soldiersPositions: SoldiersBattlePositions) : Battle {

			checkEnoughArmies(armies)
			checkAllArmiesHaveRepresentationInBattlefield(armies, soldiersPositions)
			checkSoldiersInBattlefieldBelongsToArmies(armies, soldiersPositions)
			checkPositionsAreInBattlefieldBounds(battlefield, soldiersPositions)

			return Battle(battlefield, armies, soldiersPositions)
		}


		private fun checkEnoughArmies(armies: Set<Army>) {
			if (armies.size < 2) {
				throw NotEnoughSoldiers("You need at least two armies to battle")
			}
		}


		private fun checkAllArmiesHaveRepresentationInBattlefield(
			armies: Set<Army>,
			soldiersPositions: SoldiersBattlePositions) {

			if (armies.any{
				it.soldiers.none{ soldiersPositions.containsSoldier(it) }
			}) {
				throw NotEnoughSoldiers("Not all armies are represented in the battlefield")
			}
		}

		private fun checkSoldiersInBattlefieldBelongsToArmies(
			armies: Set<Army>,
			soldiersPositions: SoldiersBattlePositions) {

			if (soldiersPositions.soldiers().any{ soldier -> armies.none{ it.containsSoldier(soldier) } }) {
				throw SoldierNotInArmy()
			}
		}

		private fun checkPositionsAreInBattlefieldBounds(battlefield: Battlefield,
																		 soldiersPositions: SoldiersBattlePositions) {
			if (!soldiersPositions.areAllWithinBounds(battlefield.boundaries)) {
				throw InvalidSoldiersPosition("Some soldiersAndPositions are out of the battlefield bounds")
			}
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

		this.soldiersStatuses = armies
			.map { army -> Pair(army, army.soldiers.map{ soldier -> Pair(soldier, SoldierStatus.FIGHTING) }.toMap()) }
			.toMap()

	}
}
