package apuchau.skirmish

import apuchau.skirmish.battle.Battle
import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BattleAcceptanceTests {

    private val KingArthur = Soldier(SoldierId("King Arthur"))
    private val Mordred = Soldier(SoldierId("Mordred"))

    @Test

    fun a_skirmish() {

        val battlefield = Battlefield(BattlefieldBoundaries(2,1))

        val armies = setOf(
           Army("Arthur's army", setOf(KingArthur)),
           Army("Mordred's army", setOf(Mordred))
        )

        val soldiersPositions = SoldiersBattlePositions(listOf(
                Pair(KingArthur, BattlefieldPosition(1,1)),
                Pair(Mordred, BattlefieldPosition(2,1)))
        )

        val battle = Battle(battlefield, armies, soldiersPositions)

        assertEquals(battle.status(), SoldiersBattlePositions(listOf(
                Pair(KingArthur, BattlefieldPosition(1,1)),
                Pair(Mordred, BattlefieldPosition(2,1)))))
    }



}
