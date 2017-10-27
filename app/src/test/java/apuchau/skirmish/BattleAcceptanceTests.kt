package apuchau.skirmish

import assertk.assert
import assertk.assertions.containsExactly
import junit.framework.Assert.assertEquals
import org.junit.Test

class BattleAcceptanceTests {

    @Test

    fun a_skirmish() {

        val battlefield = Battlefield(2, 1)
        val soldiersPossitioned = listOf(
                Pair(Soldier(), BattlefieldPosition(1,1)),
                Pair(Soldier(), BattlefieldPosition(2,1))
        )

        val battle = Battle(battlefield, soldiersPossitioned)

        assertEquals(2, battle.battlefield.width)
        assertEquals(1, battle.battlefield.height)
        assert(battle.status()).containsExactly(
                Pair(Soldier(), BattlefieldPosition(1,1)),
                Pair(Soldier(), BattlefieldPosition(2,1))
        )
    }



}
