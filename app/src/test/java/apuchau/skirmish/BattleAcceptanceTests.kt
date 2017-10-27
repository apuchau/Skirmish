package apuchau.skirmish

import assertk.assert
import assertk.assertions.containsExactly
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BattleAcceptanceTests {

    private val KingArthur = Soldier(SoldierId("King Arthur"))
    private val Mordred = Soldier(SoldierId("Mordred"))

    @Test

    fun a_skirmish() {

        val battlefield = Battlefield(2, 1)
        val soldiersPositions = listOf(
                Pair(KingArthur, BattlefieldPosition(1,1)),
                Pair(Mordred, BattlefieldPosition(2,1))
        )

        val battle = Battle(battlefield, soldiersPositions)

        assertEquals(2, battle.battlefield.width)
        assertEquals(1, battle.battlefield.height)
        assert(battle.status()).containsExactly(
                Pair(KingArthur, BattlefieldPosition(1,1)),
                Pair(Mordred, BattlefieldPosition(2,1))
        )
    }



}
