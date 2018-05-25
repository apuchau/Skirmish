package apuchau.skirmish.app

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import apuchau.skirmish.battle.log.BattleLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BattlefieldTextViewTests {

	@Test
	fun if_battlelog_empty__then_nothing_displayed() {

		val view = BattleLogView(targetContext())
		view.setLayoutParams(ViewGroup.LayoutParams(300, 100))
		val battleLog = BattleLog.empty()
		view.displayBattleLog(battleLog)

		val expectedBattleLogText = ""

		assertViewContent(expectedBattleLogText, view)
	}

	@Test
	fun if_battlelog_contains_entries__then_entries_are_displayed() {

		val view = BattleLogView(targetContext())
		view.setLayoutParams(ViewGroup.LayoutParams(300, 100))
		val battleLog= BattleLog.empty()
			.byAddingEntries(listOf(
				"This is a battle log entry",
				"This is another battle log entry"))
		view.displayBattleLog(battleLog)

		val expectedBattleLogText =
			"This is a battle log entry\n" +
				"This is another battle log entry\n"

		assertViewContent(expectedBattleLogText, view)
	}

	private fun assertViewContent(expectedText: String, view: View) {
		assertTrue(view is TextView)
		assertEquals(expectedText, (view as TextView).text)
	}

	private fun targetContext() : Context? = InstrumentationRegistry.getTargetContext()
}
