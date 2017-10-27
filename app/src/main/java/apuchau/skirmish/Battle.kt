package apuchau.skirmish

class Battle(val battlefield: Battlefield, soldiers: Collection<Pair<Soldier, BattlefieldPosition>>) {

    private val soldiersPositions: MutableList<Pair<Soldier,BattlefieldPosition>>

    init {
        soldiersPositions = soldiers.toMutableList()
    }

    fun status(): Collection<Pair<Soldier,BattlefieldPosition>> {
        return soldiersPositions.toList()
    }

}
