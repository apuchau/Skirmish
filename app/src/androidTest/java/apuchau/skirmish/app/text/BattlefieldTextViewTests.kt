package apuchau.skirmish.app.text

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.ViewGroup
import android.widget.TextView
import apuchau.skirmish.app.BattlefieldView
import apuchau.skirmish.battle.BattleSnapshot
import apuchau.skirmish.battle.SoldierAction
import apuchau.skirmish.battle.SoldierInBattle
import apuchau.skirmish.battle.SoldiersInBattle
import apuchau.skirmish.battle.log.BattleLog
import apuchau.skirmish.battlefield
import apuchau.skirmish.battlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import apuchau.skirmish.soldier.SoldierStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BattlefieldTextViewTests {

	private val KingArthur = Soldier(SoldierId("King Arthur"))
	private val Mordred = Soldier(SoldierId("Mordred"))

	private val allSoldiers = setOf(KingArthur, Mordred)

	@Test
	fun when_no_soldiers_in_battlefield__empty_battlefield_is_displayed() {

		val context = InstrumentationRegistry.getTargetContext()
		val battlefield = battlefield(3,2)

		val battleSnapshot = BattleSnapshot(
			battlefield,
			SoldiersInBattle(emptyList()),
			BattleLog.empty())

		val view = BattlefieldTextView(context)
		view.displayBattleSnapshot(snapshot = battleSnapshot)
		view.setLayoutParams(ViewGroup.LayoutParams(100, 100))
		view.displayBattleSnapshot(snapshot = battleSnapshot)

		val expectedStatusText =
			"┏━━━┓\n"+
		   "┃   ┃\n"+
		   "┃   ┃\n"+
		   "┗━━━┛\n"

		assertViewContent(expectedStatusText, view)
	}

	@Test
	fun when_soldiers_in_battlefield__soldiers_and_battlefield_are_displayed() {

		val context = InstrumentationRegistry.getTargetContext()
		val battlefield = battlefield(3,2)

		val kingArthurInBattle= SoldierInBattle.createHealthyDoingNothing(KingArthur, battlefieldPosition(1,2))
		val mordredInBattle= SoldierInBattle.createHealthyDoingNothing(Mordred, battlefieldPosition(2,2))

		val battleSnapshot = BattleSnapshot(
			battlefield,
			SoldiersInBattle(listOf(kingArthurInBattle, mordredInBattle)),
			BattleLog.empty())

		val view = BattlefieldTextView(context)
		view.displayBattleSnapshot(snapshot = battleSnapshot)
		view.setLayoutParams(ViewGroup.LayoutParams(100,100))

		val expectedStatusText =
			   "┏━━━┓\n"+
				"┃   ┃\n"+
				"┃SS ┃\n"+
				"┗━━━┛\n"

		assertViewContent(expectedStatusText, view)
	}

	@Test
	fun when_soldiers_are_dead__they_are_not_shown_in_battlefield() {

		val context = InstrumentationRegistry.getTargetContext()
		val battlefield = battlefield(3,2)

		val kingArthurInBattle= SoldierInBattle.createHealthyDoingNothing(KingArthur, battlefieldPosition(1,2))

		val mordredInBattle = SoldierInBattle(Mordred, battlefieldPosition(2,2), SoldierStatus.DEAD, SoldierAction.DO_NOTHING)

		val battleSnapshot = BattleSnapshot(
			battlefield,
			SoldiersInBattle(listOf(kingArthurInBattle, mordredInBattle)),
			BattleLog.empty())

		val view = BattlefieldTextView(context)
		view.displayBattleSnapshot(snapshot = battleSnapshot)
		view.setLayoutParams(ViewGroup.LayoutParams(100,100))

		val expectedStatusText =
			   "┏━━━┓\n"+
				"┃   ┃\n"+
				"┃S  ┃\n"+
				"┗━━━┛\n"

		assertViewContent(expectedStatusText, view)
	}

	private fun assertViewContent(expectedText: String, view: BattlefieldView) {
		assertTrue(view is TextView)
		assertEquals(expectedText, (view as TextView).text)
	}
}