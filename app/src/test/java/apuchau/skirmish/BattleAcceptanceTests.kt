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
    fun a_skirmish_with_two_soldiers_next_to_each_other_and_fighting_to_death() {

		 val battlefield = createBattlefield(2,1)

		 val arthursArmy = createArmy("Arthur's army", setOf(KingArthur))
		 val mordredsArmy = createArmy("Mordred's army", setOf(Mordred))

		 val armies = setOf(arthursArmy, mordredsArmy)

		 val startingBattlePositions = SoldiersBattlePositions(listOf(
			 Pair(KingArthur, battlefieldPosition(1,1)),
			 Pair(Mordred, battlefieldPosition(2,1))
		 ))

		 val battle = createBattle(battlefield, armies, startingBattlePositions)

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

	@Test
	fun a_skirmish_with_two_soldiers_not_next_to_each_other_cant_fight() {

		val battlefield = createBattlefield(3,1)

		val arthursArmy = createArmy("Arthur's army", setOf(KingArthur))
		val mordredsArmy = createArmy("Mordred's army", setOf(Mordred))

		val armies = setOf(arthursArmy, mordredsArmy)

		val startingBattlePositions = SoldiersBattlePositions(listOf(
			Pair(KingArthur, battlefieldPosition(1,1)),
			Pair(Mordred, battlefieldPosition(3,1))
		))

		val battle = createBattle(battlefield, armies, startingBattlePositions)

		var expectedSnapshot = BattleSnapshot(
			battlefield,
			startingBattlePositions,
			mapOf(
				Pair( arthursArmy, mapOf(Pair(KingArthur, SoldierStatus.DOING_NOTHING))),
				Pair( mordredsArmy, mapOf(Pair(Mordred, SoldierStatus.DOING_NOTHING)))
			))

		assertEquals(battle.snapshot(), expectedSnapshot)

		// If two soldiers of are not next to each other, they don't fight

		battle.timeCycle()

		expectedSnapshot = BattleSnapshot(
			battlefield,
			startingBattlePositions,
			mapOf(
				Pair( arthursArmy, mapOf(Pair(KingArthur, SoldierStatus.DOING_NOTHING))),
				Pair( mordredsArmy, mapOf(Pair(Mordred, SoldierStatus.DOING_NOTHING)))
			))

		assertEquals(battle.snapshot(), expectedSnapshot)
	}

	private fun createBattle(battlefield: Battlefield,
									 armies: Set<Army>,
									 soldiersPositions: SoldiersBattlePositions): Battle {
		return Battle.instance(battlefield, armies, soldiersPositions)
			.onError { throw Exception("Can't create battle. ${it.reason}") }
	}

	private fun createBattlefield(width: Int, height: Int): Battlefield =
		Battlefield(
			BattlefieldBoundaries.create(width, height)
				.onError { throw Exception("Invalid boundaries. ${it.reason}") })

	private fun createArmy(armyId: String, soldiers: Set<Soldier>) : Army =
		Army.create(armyId, soldiers)
			.onError( { throw Exception("Can't create army. ${it.reason}") })

}
