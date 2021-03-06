package apuchau.skirmish

import apuchau.skirmish.army.Army
import apuchau.skirmish.battle.*
import apuchau.skirmish.battle.log.BattleLog
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.battlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import com.natpryce.onError
import junit.framework.TestCase.assertEquals
import org.junit.Test

class Battle1vs1SoldiersAcceptanceTests {

	private val battlefield = createBattlefield(3,1)

	private val KingArthur = Soldier(SoldierId("King Arthur"))
	private val Mordred = Soldier(SoldierId("Mordred"))

	private val arthursArmy = createArmy("Arthur's army", setOf(KingArthur))
	private val mordredsArmy = createArmy("Mordred's army", setOf(Mordred))

	private val armies = setOf(arthursArmy, mordredsArmy)

    @Test
    fun a_skirmish_with_two_soldiers_next_to_each_other_and_fighting_to_death() {

		 val kingArthurInBattle = SoldierInBattle.createHealthyDoingNothing(
			 KingArthur, battlefieldPosition(1,1))

		 val mordredInBattle = SoldierInBattle.createHealthyDoingNothing(
			 Mordred, battlefieldPosition(2,1))

		 val battle= createBattle(battlefield, armies, listOf(kingArthurInBattle, mordredInBattle))

		 assertBattleSnapshot(battle.snapshot())
			 .assertAllSoldiersIdle()
			 .assertAllSoldiersHealthy()
			 .assertLog(
				 listOf(
					 "Soldier King Arthur starts at position (1, 1)",
					 "Soldier Mordred starts at position (2, 1)"
				 ))

		 // If two soldiers are next to each other, they fight

		 battle.timeCycle()

		 assertBattleSnapshot(battle.snapshot())
			 .assertAllSoldiersBattling()
			 .assertSoldierIsWounded(KingArthur)
			 .assertSoldierIsWounded(Mordred)
			 .assertLog(
				 listOf(
					 "Soldier King Arthur starts at position (1, 1)",
					 "Soldier Mordred starts at position (2, 1)",
					 "King Arthur started fighting",
					 "Mordred started fighting",
					 "King Arthur hit Mordred who is now WOUNDED",
					 "Mordred hit King Arthur who is now WOUNDED"
				 ))

		 battle.timeCycle()

		 assertBattleSnapshot(battle.snapshot())
			 .assertSoldierIsWounded(KingArthur)
			 .assertSoldierIsDead(Mordred)
			 .assertLog(
				 listOf(
					 "Soldier King Arthur starts at position (1, 1)",
					 "Soldier Mordred starts at position (2, 1)",
					 "King Arthur started fighting",
					 "Mordred started fighting",
					 "King Arthur hit Mordred who is now WOUNDED",
					 "Mordred hit King Arthur who is now WOUNDED",
					 "King Arthur hit Mordred who is now DEAD"
				 ))

		 // After a cycle nothing should change, dead people shouldn't fight and
		 //  alive people shouldn't hit to dead people

		 battle.timeCycle()

		 assertBattleSnapshot(battle.snapshot())
			 .assertSoldierIsWounded(KingArthur)
			 .assertSoldierIsDead(Mordred)
			 .assertLog(
				 listOf(
					 "Soldier King Arthur starts at position (1, 1)",
					 "Soldier Mordred starts at position (2, 1)",
					 "King Arthur started fighting",
					 "Mordred started fighting",
					 "King Arthur hit Mordred who is now WOUNDED",
					 "Mordred hit King Arthur who is now WOUNDED",
					 "King Arthur hit Mordred who is now DEAD"
				 ))
    }

	private fun assertBattleSnapshot(snapshot: BattleSnapshot): BattleSnapshotAsserter =
		BattleSnapshotAsserter(snapshot)

	@Test
	fun a_skirmish_with_two_soldiers_not_next_to_each_other_cant_fight() {

		val kingArthurInBattle = SoldierInBattle.createHealthyDoingNothing(
			KingArthur, battlefieldPosition(1,1))

		val mordredInBattle = SoldierInBattle.createHealthyDoingNothing(
			Mordred, battlefieldPosition(3,1))

		val battle = createBattle(battlefield, armies, listOf(kingArthurInBattle, mordredInBattle))

		var expectedSnapshot = BattleSnapshot(
			battlefield,
			SoldiersInBattle(listOf(kingArthurInBattle, mordredInBattle)),
			BattleLog.empty()
		)

		assertEquals(battle.snapshot(), expectedSnapshot)

		// If two soldiers of are not next to each other, they don't fight

		battle.timeCycle()

		expectedSnapshot = BattleSnapshot(
			battlefield,
			SoldiersInBattle(listOf(kingArthurInBattle, mordredInBattle)),
			BattleLog.empty()
		)

		assertEquals(battle.snapshot(), expectedSnapshot)
	}

	private fun createBattle(battlefield: Battlefield,
									 armies: Set<Army>,
									 soldiersInBattle: Collection<SoldierInBattle>): Battle {
		return Battle.instance(battlefield, armies, soldiersInBattle)
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
