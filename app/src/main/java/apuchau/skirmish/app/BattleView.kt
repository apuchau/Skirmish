package apuchau.skirmish.app

import android.content.Context
import android.widget.LinearLayout
import apuchau.skirmish.app.text.BattlefieldTextView
import apuchau.skirmish.battle.BattleSnapshot

class BattleView(context: Context?) : LinearLayout(context) {

	private val battlefieldView : BattlefieldView
	private val battleLogView : BattleLogView

	init {
		layout(0, 0, 0, 0)
		orientation = LinearLayout.VERTICAL

		battlefieldView = BattlefieldTextView(context)
		addView(battlefieldView)

		battleLogView = BattleLogView(context)
		addView(battleLogView)
	}

	fun displayBattleSnapshot(snapshot: BattleSnapshot) {
		battlefieldView.displayBattleSnapshot(snapshot)
	}
}