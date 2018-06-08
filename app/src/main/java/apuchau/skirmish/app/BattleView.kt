package apuchau.skirmish.app

import android.content.Context
import android.graphics.Canvas
import android.widget.LinearLayout
import apuchau.skirmish.app.text.BattlefieldTextView
import apuchau.skirmish.battle.BattleSnapshot

class BattleView(context: Context?) : LinearLayout(context) {

	private val battlefieldView : BattlefieldView
	private val battleLogView : BattleLogView

	private var currentSnapshot: BattleSnapshot? = null

	init {
		layout(0, 0, 0, 0)
		orientation = LinearLayout.VERTICAL

		battlefieldView = BattlefieldTextView(context)
		battlefieldView.width = 1000
		battlefieldView.height = 500
		addView(battlefieldView)

		battleLogView = BattleLogView(context)
		battleLogView.width = 1000
		battleLogView.height = 700
		addView(battleLogView)

		setWillNotDraw(false)	// Needed so the system calls custom onDraw method
	}

	fun displayBattleSnapshot(snapshot: BattleSnapshot) {
		currentSnapshot = snapshot
		invalidate()
	}

	override fun onDraw(canvas: Canvas) {
		currentSnapshot?.let {
			battlefieldView.displayBattleSnapshot(it)
			battleLogView.displayBattleLog(it.battleLog)
		}
	}
}