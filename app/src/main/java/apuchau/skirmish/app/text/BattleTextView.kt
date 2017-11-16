package apuchau.skirmish.app.text

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import apuchau.skirmish.app.BattleView
import apuchau.skirmish.battle.BattleSnapshot

class BattleTextView(context: Context?) : TextView(context), BattleView {

	init {
		this.setTypeface(Typeface.MONOSPACE)
		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40F)
	}

	override fun displayBattleSnapshot(snapshot: BattleSnapshot) {
		setText(createBattleStatusTextVersion(snapshot))
	}

	private fun createBattleStatusTextVersion(battleSnapshot: BattleSnapshot): String {

		val boundaries = battleSnapshot.battlefield.boundaries

		val topBorder = "┏" + "━".repeat(boundaries.width) + "┓\n"
		val emptyRow = "┃" + " ".repeat(boundaries.width) + "┃\n"
		val bottomBorder = "┗" + "━".repeat(boundaries.width) + "┛\n"

		val rows = (1 .. boundaries.height).map { emptyRow }.joinToString("")

		return topBorder + rows + bottomBorder
	}
}