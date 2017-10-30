package apuchau.skirmish

import junit.framework.TestCase.assertEquals
import org.junit.Test

class BattleAcceptanceTests {

    private val KingArthur = Soldier(SoldierId("King Arthur"))
    private val Mordred = Soldier(SoldierId("Mordred"))

    @Test

    fun a_skirmish() {

        val battlefield = Battlefield(2, 1)
        val soldiersPositions = SoldiersBattlePositions(listOf(
                Pair(KingArthur, BattlefieldPosition(1,1)),
                Pair(Mordred, BattlefieldPosition(2,1)))
        )

        val battle = Battle(battlefield, soldiersPositions)

        assertEquals(2, battle.battlefield.width)
        assertEquals(1, battle.battlefield.height)
        assertEquals(battle.status(), SoldiersBattlePositions(listOf(
                Pair(KingArthur, BattlefieldPosition(1,1)),
                Pair(Mordred, BattlefieldPosition(2,1)))))
    }



}
