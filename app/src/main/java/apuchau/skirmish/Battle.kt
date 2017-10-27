package apuchau.skirmish

class Battle(val battlefield: Battlefield, soldiers: Collection<Pair<Soldier, BattlefieldPosition>>) {

    private val soldiers: MutableList<Pair<Soldier,BattlefieldPosition>>

    init {
        checkSoldiersDontOccupySameSpaces(soldiers);
        this.soldiers = soldiers.toMutableList()
    }

    private fun checkSoldiersDontOccupySameSpaces(soldiers: Collection<Pair<Soldier, BattlefieldPosition>>) {

		 val numDifferentPositions =
			 soldiers
				 .map(Pair<Soldier,BattlefieldPosition>::second)
				 .distinct()
				 .count()

		 if (soldiers.size != numDifferentPositions) {
			 throw InvalidSoldiersPosition("Some soldiers occupy the same battlefield spot");
		 }

    }

    fun status(): Collection<Pair<Soldier,BattlefieldPosition>> {
        return soldiers.toList()
    }

}
