package apuchau.skirmish

import apuchau.skirmish.exception.BattleWithNoSoldiers
import apuchau.skirmish.exception.DuplicatedSoldier
import apuchau.skirmish.exception.InvalidSoldiersPosition

class Battle(val battlefield: Battlefield, soldiersPositions: SoldiersBattlePositions) {

    private val soldiersPositions: SoldiersBattlePositions

    init {
		 checkEnoughSoldiersForBattle(soldiersPositions)

		 this.soldiersPositions = soldiersPositions
    }

	private fun checkEnoughSoldiersForBattle(soldiersPositions: SoldiersBattlePositions) {
		if (soldiersPositions.count() < 2) {
			throw BattleWithNoSoldiers("Battle can't happen without soldiers")
		}
	}

    fun status(): SoldiersBattlePositions {
        return soldiersPositions
    }

}
