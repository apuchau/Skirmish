package apuchau.skirmish.app

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.ViewGroup
import android.widget.TextView
import apuchau.skirmish.app.text.BattleTextView
import apuchau.skirmish.battle.BattleSnapshot
import apuchau.skirmish.battle.SoldiersBattleActions
import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import com.natpryce.onError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BattleTextViewTests {

	private val KingArthur = Soldier(SoldierId("King Arthur"))
	private val Mordred = Soldier(SoldierId("Mordred"))

	@Test
	fun when_no_soldiers_in_battlefield__empty_battlefield_is_displayed() {

		val context = InstrumentationRegistry.getTargetContext()
		val battlefield = createBattlefield(3,2)
		val battlePositions = SoldiersBattlePositions(emptyList())
		val soldiersActions = SoldiersBattleActions.withAllDoingNothing(setOf(KingArthur))
		val battleSnapshot = BattleSnapshot(battlefield, battlePositions, soldiersActions)

		val view = BattleTextView(context)
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
		val battlefield = createBattlefield(3,2)
		val battlePositions = SoldiersBattlePositions(listOf(
			Pair(KingArthur, battlefieldPosition(1,2)),
			Pair(Mordred, battlefieldPosition(2, 2))
		))
		val soldiersActions = SoldiersBattleActions.withAllDoingNothing(setOf(KingArthur))
		val battleSnapshot = BattleSnapshot(battlefield, battlePositions, soldiersActions)

		val view = BattleTextView(context)
		view.displayBattleSnapshot(snapshot = battleSnapshot)
		view.setLayoutParams(ViewGroup.LayoutParams(100,100))

		val expectedStatusText =
			   "┏━━━┓\n"+
				"┃   ┃\n"+
				"┃SS ┃\n"+
				"┗━━━┛\n"

		assertViewContent(expectedStatusText, view)
	}

	private fun assertViewContent(expectedText: String, view: BattleView) {
		assertTrue(view is TextView)
		assertEquals(expectedText, (view as TextView).text)
	}

	private fun createBattlefield(width: Int, height: Int): Battlefield {
		return Battlefield(
			BattlefieldBoundaries.create(width, height)
				.onError { throw Exception("Invalid boundaries. ${it.reason}") })
	}

	private fun battlefieldPosition(x: Int, y: Int): BattlefieldPosition =
		BattlefieldPosition.create(x,y).onError { throw Exception("Error creating battlefield position") }

}