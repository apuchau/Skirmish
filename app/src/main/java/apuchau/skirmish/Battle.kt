package apuchau.skirmish

import apuchau.skirmish.exception.NotEnoughSoldiersToBattle
import apuchau.skirmish.exception.InvalidSoldiersPosition

class Battle(private val battlefield: Battlefield, soldiersPositions: SoldiersBattlePositions) {

	private val soldiersPositions: SoldiersBattlePositions

	init {
		checkEnoughSoldiersForBattle(soldiersPositions)
		checkPositionsAreInBattlefieldBounds(battlefield, soldiersPositions)
		this.soldiersPositions = soldiersPositions
	}

	private fun checkPositionsAreInBattlefieldBounds(battlefield: Battlefield,
																	 soldiersPositions: SoldiersBattlePositions) {
		if (!soldiersPositions.areAllWithinBounds(battlefield.boundaries)) {
			throw InvalidSoldiersPosition("Some soldiersAndPositions are out of the battlefield bounds")
		}
	}

	private fun checkEnoughSoldiersForBattle(soldiersPositions: SoldiersBattlePositions) {
		if (soldiersPositions.count() < 2) {
			throw NotEnoughSoldiersToBattle("You need at least two soldiers to start a battle")
		}
	}

	fun status(): SoldiersBattlePositions {
		return soldiersPositions
	}

	override fun toString(): String {
		return "Battle. Battlefield: $battlefield, Soldiers: $soldiersPositions"
	}
}
