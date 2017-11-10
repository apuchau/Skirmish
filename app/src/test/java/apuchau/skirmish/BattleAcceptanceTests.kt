package apuchau.skirmish

import apuchau.skirmish.army.Army
import apuchau.skirmish.battle.Battle
import apuchau.skirmish.battle.BattleSnapshot
import apuchau.skirmish.battle.SoldierStatus
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

		 val arthursArmy = Army("Arthur's army", setOf(KingArthur))
		 val mordredsArmy = Army("Mordred's army", setOf(Mordred))

		 val armies = setOf(arthursArmy, mordredsArmy)

		 val soldiersPositions = SoldiersBattlePositions(listOf(
			 Pair(KingArthur, BattlefieldPosition(1,1)),
			 Pair(Mordred, BattlefieldPosition(2,1))
		 ))

		 val battle = Battle(battlefield, armies, soldiersPositions)

		 val startingBattlePositions = SoldiersBattlePositions(listOf(
			 Pair(KingArthur, BattlefieldPosition(1, 1)),
			 Pair(Mordred, BattlefieldPosition(2, 1))
		 ))

		 val soldiersStatuses = mapOf(
			 Pair( arthursArmy, mapOf(Pair(KingArthur, SoldierStatus.DOING_NOTHING))),
			 Pair( mordredsArmy, mapOf(Pair(Mordred, SoldierStatus.DOING_NOTHING)))
		 )

		 val expectedSnapshot = BattleSnapshot(
			 battlefield,
			 startingBattlePositions,
			 soldiersStatuses)

		 assertEquals(battle.snapshot(), expectedSnapshot)

		 // Nothing changes in the battle after a time cycle

		 battle.timeCycle()

		 assertEquals(battle.snapshot(), expectedSnapshot)
    }



}
