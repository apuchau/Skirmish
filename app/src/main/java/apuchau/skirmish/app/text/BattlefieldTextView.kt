package apuchau.skirmish.app.text

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import apuchau.skirmish.app.BattlefieldView
import apuchau.skirmish.battle.BattleSnapshot

class BattlefieldTextView(context: Context?) : TextView(context), BattlefieldView {

	init {
		setTypeface(Typeface.createFromAsset(context?.resources?.assets, "fonts/F25_Bank_Printer.ttf"))
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 40F)
		paint.setAntiAlias(false)
		setBackgroundColor(Color.WHITE)
		setTextColor(Color.BLACK)
	}

	override fun displayBattleSnapshot(snapshot: BattleSnapshot) {
		setText(createBattleStatusTextVersion(snapshot))
		invalidate()
	}

	private fun createBattleStatusTextVersion(battleSnapshot: BattleSnapshot): String {
		return textRepresentation(battleSnapshot)
	}
}