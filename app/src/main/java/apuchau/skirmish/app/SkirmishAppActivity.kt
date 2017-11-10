package apuchau.skirmish.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import apuchau.skirmish.app.text.BattleTextView
import apuchau.skirmish.army.Army
import apuchau.skirmish.battle.Battle
import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId


class SkirmishAppActivity : Activity() {

	private var battle : Battle? = null
	private var battleView : BattleView? = null

	private val KingArthur = Soldier(SoldierId("King Arthur"))
	private val Mordred = Soldier(SoldierId("Mordred"))

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		createBattle()
		createBattleView()
	}

	private fun createBattle() {

		val battlefield = Battlefield(BattlefieldBoundaries(2,1))

		val armies = setOf(
			Army("Arthur's army", setOf(KingArthur)),
			Army("Mordred's army", setOf(Mordred))
		)

		val soldiersPositions = SoldiersBattlePositions(listOf(
			Pair(KingArthur, BattlefieldPosition(1,1)),
			Pair(Mordred, BattlefieldPosition(2,1)))
		)

		battle = Battle(battlefield, armies, soldiersPositions)
	}

	fun createBattleView() {

		var rootView : View = getWindow().getDecorView().getRootView()

		val layout = LinearLayout(this)
		layout.layout(5, 5, 5, 5)
		layout.layoutParams = ViewGroup.LayoutParams(rootView.width, rootView.height)
		layout.orientation = LinearLayout.VERTICAL
		setContentView(layout)

		val battleView = BattleTextView(this)
		layout.addView(battleView)

		displayBattle()
	}

	fun displayBattle() {

		var battle = this.battle ?: return
		this.battleView?.displayBattleStatus((battle.battlefield))
	}

}
