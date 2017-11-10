package apuchau.skirmish.app

import apuchau.skirmish.Battlefield
import apuchau.skirmish.BattlefieldBoundaries
import apuchau.skirmish.app.text.BattleTextView
import org.junit.Test
import android.widget.TextView
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.ViewGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BattleTextViewTests {

	@Test
	fun when_no_soldiers_and_battlefield_empty_battlefield_is_displayed() {

		val context = InstrumentationRegistry.getTargetContext()
		val battlefield = Battlefield(BattlefieldBoundaries(3,2))

		val view = BattleTextView(context)
		view.displayBattleStatus(battlefield)
		view.setLayoutParams(ViewGroup.LayoutParams(100, 100))

		val expectedStatusText =
			"┏━━━┓\n"+
		   "┃   ┃\n"+
		   "┃   ┃\n"+
		   "┗━━━┛\n"

		assertViewContent(expectedStatusText, view)
	}

	private fun assertViewContent(expectedText: String, view: BattleView) {
		assertTrue(view is TextView)
		assertEquals(expectedText, (view as TextView).text)
	}


}