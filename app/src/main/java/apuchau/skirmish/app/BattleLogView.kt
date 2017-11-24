package apuchau.skirmish.app

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import apuchau.skirmish.battle.log.BattleLog

class BattleLogView(context: Context?) : TextView(context) {

	init {
		this.setTypeface(Typeface.DEFAULT)
		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
	}

	fun displayBattleLog(battleLog: BattleLog) {
		val strBuilder = StringBuilder()
		battleLog.forEachEntry {
			entry -> strBuilder.appendln(entry)
		}
		setText(strBuilder.toString())
	}
}