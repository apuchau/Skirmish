package apuchau.skirmish.app

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import apuchau.skirmish.app.view.subviews
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BattleViewTests {

	@Test
	fun battleView_contains_battlefieldView_and_logView() {

		val battleView = BattleView(targetContext())

		assertEquals("Subviews", 2, battleView.subviews().size)
		assertTrue(battleView.subviews().get(0) is BattlefieldView)
		assertTrue(battleView.subviews().get(1) is BattleLogView)
	}

	private fun targetContext(): Context? = InstrumentationRegistry.getTargetContext()

}
