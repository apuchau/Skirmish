package apuchau.skirmish

import apuchau.skirmish.exception.NotEnoughSoldiers
import apuchau.skirmish.exception.InvalidSoldiersPosition

class Battle(private val battlefield: Battlefield,
				 private val armies: Set<Army>,
				 private val soldiersPositions: SoldiersBattlePositions) {

	init {
		checkEnoughArmies(armies)
		checkEnoughSoldiersForBattle(soldiersPositions)
		checkPositionsAreInBattlefieldBounds(battlefield, soldiersPositions)
	}

	private fun checkEnoughArmies(armies: Set<Army>) {
		if (armies.size < 2) {
			throw NotEnoughSoldiers("You need at least two armies to battle")
		}
	}

	private fun checkPositionsAreInBattlefieldBounds(battlefield: Battlefield,
																	 soldiersPositions: SoldiersBattlePositions) {
		if (!soldiersPositions.areAllWithinBounds(battlefield.boundaries)) {
			throw InvalidSoldiersPosition("Some soldiersAndPositions are out of the battlefield bounds")
		}
	}

	private fun checkEnoughSoldiersForBattle(soldiersPositions: SoldiersBattlePositions) {
		if (soldiersPositions.count() < 2) {
			throw NotEnoughSoldiers("You need at least two soldiers to start a battle")
		}
	}

	fun status(): SoldiersBattlePositions {
		return soldiersPositions
	}

	override fun toString(): String {
		return "Battle. Battlefield: $battlefield, Armies: $armies, Positions: $soldiersPositions"
	}
}
