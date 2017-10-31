package apuchau.skirmish

import apuchau.skirmish.exception.NotEnoughSoldiers
import apuchau.skirmish.exception.InvalidSoldiersPosition
import apuchau.skirmish.exception.SoldierNotInArmy

class Battle(private val battlefield: Battlefield,
				 private val armies: Set<Army>,
				 private val soldiersPositions: SoldiersBattlePositions) {

	init {
		checkEnoughArmies(armies)
		checkAllArmiesHaveRepresentationInBattlefield(armies, soldiersPositions)
		checkSoldiersInBattlefieldBelongsToArmies(armies, soldiersPositions)
		checkPositionsAreInBattlefieldBounds(battlefield, soldiersPositions)
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

	fun status(): SoldiersBattlePositions {
		return soldiersPositions
	}

	override fun toString(): String {
		return "Battle. Battlefield: $battlefield, Armies: $armies, Positions: $soldiersPositions"
	}
}
