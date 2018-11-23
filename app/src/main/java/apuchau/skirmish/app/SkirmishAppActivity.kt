package apuchau.skirmish.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import apuchau.skirmish.app.view.takeSnapshot
import apuchau.skirmish.army.Army
import apuchau.skirmish.battle.Battle
import apuchau.skirmish.battle.SoldierAction
import apuchau.skirmish.battle.SoldierInBattle
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import apuchau.skirmish.soldier.SoldierStatus
import com.natpryce.onError
import java.util.*
import kotlin.concurrent.timerTask

class SkirmishAppActivity : Activity() {

	private var battle : Battle? = null
	private var battleView: BattleView? = null

	private val KingArthur = Soldier(SoldierId("King Arthur"))
	private val Mordred = Soldier(SoldierId("Mordred"))

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		createBattle()
		createBattleView()
		startBattle()
	}

	fun startBattle() {
		displayBattle()
		startBattleCycleTimer()
	}

	private fun createBattle() {

		val battlefield = Battlefield(createBattlefieldBoundaries(5,3))

		val armies = setOf(
			createArmy("Arthur's army", setOf(KingArthur)),
			createArmy("Mordred's army", setOf(Mordred))
		)

		val kingArthurInBattle = SoldierInBattle(
			KingArthur,
			battlefieldPosition(3,2),
			SoldierStatus.HEALTHY,
			SoldierAction.DO_NOTHING)

		val mordredInBattle = SoldierInBattle(
			Mordred,
			battlefieldPosition(4,2),
			SoldierStatus.HEALTHY,
			SoldierAction.DO_NOTHING)

		battle = Battle.instance(battlefield, armies, listOf(kingArthurInBattle, mordredInBattle))
			.onError { throw Exception("Can't create battle. ${it.reason}") }
	}

	private fun createBattlefieldBoundaries(width: Int, height: Int): BattlefieldBoundaries {
		return BattlefieldBoundaries.create(width, height)
			.onError { throw Exception("Invalid boundaries. ${it.reason}") }
	}

	fun createBattleView() {

		val rootView : View = getWindow().getDecorView().getRootView()

		val layout = LinearLayout(this)
		layout.layout(0, 0, 0, 0)
		layout.layoutParams = ViewGroup.LayoutParams(rootView.width, rootView.height)
		layout.orientation = LinearLayout.VERTICAL

		battleView = BattleView(this)
		setContentView(battleView)
	}

	private fun startBattleCycleTimer() {
		Timer("SkimishAppBattleTimeCycle", true).scheduleAtFixedRate(
			timerTask({ battleTimerTick() }),
			200, 3000)
	}

	private fun battleTimerTick() {
		processBattleCycle()
		displayBattle()
	}

	private fun processBattleCycle() {
		battle?.timeCycle()
	}

	fun displayBattle() {
		val battleViewSnapshot = battleView
		battle?.let { battleViewSnapshot?.displayBattleSnapshot(it.snapshot()) }
		battleViewSnapshot?.let {

			(battleViewSnapshot as View).getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
				override fun onGlobalLayout() {
					battleViewSnapshot.getViewTreeObserver().removeOnGlobalLayoutListener(this)
					(battleViewSnapshot as View).takeSnapshot(applicationContext, "battleview screenshot.png")
				}
			})
		}
	}

	fun battlefieldPosition(x: Int, y: Int) =
		BattlefieldPosition.create(x,y)
			.onError { throw Exception("Can't create position. ${it.reason}") }

	fun createArmy(armyId: String, soldiers: Set<Soldier>) =
		Army.create(armyId, soldiers)
			.onError { throw Exception("Can't create army. ${it.reason}") }
}
