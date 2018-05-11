package apuchau.skirmish.app.text

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.ViewGroup
import android.widget.TextView
import apuchau.skirmish.app.BattlefieldView
import apuchau.skirmish.battle.BattleSnapshot
import apuchau.skirmish.battle.SoldiersBattleActions
import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battle.SoldiersStatuses
import apuchau.skirmish.battle.log.BattleLog
import apuchau.skirmish.battlefield
import apuchau.skirmish.battlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
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
		val soldiersStatuses = SoldiersStatuses.withAllHealthy(allSoldiers)
		val battlePositions = SoldiersBattlePositions(emptyList())
		val soldiersActions = SoldiersBattleActions.withAllDoingNothing(allSoldiers)
		val battleSnapshot = BattleSnapshot(battlefield, soldiersStatuses, battlePositions, soldiersActions, BattleLog.empty())

		val view = BattlefieldTextView(context)
		view.displayBattleSnapshot(snapshot = battleSnapshot)
		view.setLayoutParams(ViewGroup.LayoutParams(100, 100))

		val expectedStatusText =
			"┏━━━┓\n"+
		   "┃   ┃\n"+
		   "┃   ┃\n"+
		   "┗━━━┛\n"

		assertViewContent(expectedStatusText, view)
	}

	@Test
	fun when_soldiers_in_battlefield__soldiers_and_battlefield_is_displayed() {

		val context = InstrumentationRegistry.getTargetContext()
		val battlefield = battlefield(3,2)
		val soldiersStatuses = SoldiersStatuses.withAllHealthy(allSoldiers)
		val battlePositions = SoldiersBattlePositions(listOf(
			Pair(KingArthur, battlefieldPosition(1,2)),
			Pair(Mordred, battlefieldPosition(2, 2))
		))
		val soldiersActions = SoldiersBattleActions.withAllDoingNothing(allSoldiers)
		val battleSnapshot = BattleSnapshot(battlefield, soldiersStatuses, battlePositions, soldiersActions, BattleLog.empty())

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

	private fun assertViewContent(expectedText: String, view: BattlefieldView) {
		assertTrue(view is TextView)
		assertEquals(expectedText, (view as TextView).text)
	}
}