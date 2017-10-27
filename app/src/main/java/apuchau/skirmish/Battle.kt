package apuchau.skirmish

import apuchau.skirmish.exception.BattleWithNoSoldiers
import apuchau.skirmish.exception.DuplicatedSoldier
import apuchau.skirmish.exception.InvalidSoldiersPosition

class Battle(val battlefield: Battlefield, soldiersPositions: Collection<Pair<Soldier, BattlefieldPosition>>) {

    private val soldiersPositions: MutableList<Pair<Soldier,BattlefieldPosition>>

    init {
		 checkEnoughSoldiersForBattle(soldiersPositions);
		 checkSoldiersDontOccupySameSpaces(soldiersPositions);
		 checkNoDuplicateSoldiers(soldiersPositions);
		 this.soldiersPositions = soldiersPositions.toMutableList()
    }

	private fun checkEnoughSoldiersForBattle(soldiers: Collection<Pair<Soldier, BattlefieldPosition>>) {
		if (soldiers.isEmpty()) {
			throw BattleWithNoSoldiers("Battle can't happen without soldiersPositions")
		}
	}

	private fun checkNoDuplicateSoldiers(soldiers: Collection<Pair<Soldier, BattlefieldPosition>>) {

		var numDifferentSoldiers =
			soldiers
				.map{it.first}
				.distinct()
				.count();

		if (soldiers.size != numDifferentSoldiers) {
			throw DuplicatedSoldier("There are duplicated soldiersPositions in the battle")
		}
	}

	private fun checkSoldiersDontOccupySameSpaces(soldiers: Collection<Pair<Soldier, BattlefieldPosition>>) {

		 val numDifferentPositions =
			 soldiers
				 .map{it.second}
				 .distinct()
				 .count()

		 if (soldiers.size != numDifferentPositions) {
			 throw InvalidSoldiersPosition("Some soldiersPositions occupy the same battlefield spot");
		 }

    }

    fun status(): Collection<Pair<Soldier,BattlefieldPosition>> {
        return soldiersPositions.toList()
    }

}
