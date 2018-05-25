package apuchau.skirmish.app

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import apuchau.skirmish.battle.log.BattleLog

class BattleLogView(context: Context?) : TextView(context) {

	init {
		setTypeface(Typeface.DEFAULT)
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
		setBackgroundColor(Color.WHITE)
		setTextColor(Color.BLACK)
	}

	fun displayBattleLog(battleLog: BattleLog) {
		val strBuilder = StringBuilder()
		battleLog.forEachEntry {
			entry -> strBuilder.appendln(entry)
		}
		setText(strBuilder.toString())
	}
}