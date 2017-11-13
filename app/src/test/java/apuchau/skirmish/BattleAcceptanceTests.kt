package apuchau.skirmish

import apuchau.skirmish.army.Army
import apuchau.skirmish.battle.Battle
import apuchau.skirmish.battle.BattleSnapshot
import apuchau.skirmish.battle.SoldierStatus
import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.battlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import com.natpryce.onError
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BattleAcceptanceTests {

    private val KingArthur = Soldier(SoldierId("King Arthur"))
    private val Mordred = Soldier(SoldierId("Mordred"))

    @Test

    fun a_skirmish() {

		 val battlefield = createBattlefield(2,1)

		 val arthursArmy = Army("Arthur's army", setOf(KingArthur))
		 val mordredsArmy = Army("Mordred's army", setOf(Mordred))

		 val armies = setOf(arthursArmy, mordredsArmy)

		 val soldiersPositions = SoldiersBattlePositions(listOf(
			 Pair(KingArthur, battlefieldPosition(1,1)),
			 Pair(Mordred, battlefieldPosition(2,1))
		 ))

		 val battle = createBattle(battlefield, armies, soldiersPositions)

		 val startingBattlePositions = SoldiersBattlePositions(listOf(
			 Pair(KingArthur, battlefieldPosition(1, 1)),
			 Pair(Mordred, battlefieldPosition(2, 1))
		 ))

		 var expectedSnapshot = BattleSnapshot(
			 battlefield,
			 startingBattlePositions,
			 mapOf(
				 Pair( arthursArmy, mapOf(Pair(KingArthur, SoldierStatus.DOING_NOTHING))),
				 Pair( mordredsArmy, mapOf(Pair(Mordred, SoldierStatus.DOING_NOTHING)))
			 ))

		 assertEquals(battle.snapshot(), expectedSnapshot)

		 // If two soldiers of are next to each other, they fight

		 battle.timeCycle()

		 expectedSnapshot = BattleSnapshot(
			 battlefield,
			 startingBattlePositions,
			 mapOf(
				 Pair( arthursArmy, mapOf(Pair(KingArthur, SoldierStatus.FIGHTING))),
				 Pair( mordredsArmy, mapOf(Pair(Mordred, SoldierStatus.FIGHTING)))
			 ))

		 assertEquals(battle.snapshot(), expectedSnapshot)
    }

	private fun createBattle(battlefield: Battlefield,
									 armies: Set<Army>,
									 soldiersPositions: SoldiersBattlePositions): Battle {
		return Battle.instance(battlefield, armies, soldiersPositions)
			.onError { throw Exception("Can't create battle. ${it.reason}") }
	}


	private fun createBattlefield(width: Int, height: Int): Battlefield {
		return Battlefield(
			BattlefieldBoundaries.create(width, height)
				.onError { throw Exception("Invalid boundaries. ${it.reason}") })
	}


}
