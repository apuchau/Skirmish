package apuchau.skirmish.app.text

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import apuchau.skirmish.app.BattlefieldView
import apuchau.skirmish.battle.BattleSnapshot
import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition

class BattlefieldTextView(context: Context?) : TextView(context), BattlefieldView {

	init {
		this.setTypeface(Typeface.MONOSPACE)
		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40F)
	}

	override fun displayBattleSnapshot(snapshot: BattleSnapshot) {
		setText(createBattleStatusTextVersion(snapshot))
	}

	private fun createBattleStatusTextVersion(battleSnapshot: BattleSnapshot): String {

		val battlefieldTxt = createBattlefield(battleSnapshot.battlefield)
		return displaySoldiersInBattlefield(
			battlefieldTxt,
			battleSnapshot.battlefield.boundaries,
			battleSnapshot.soldiersBattlePositions)
	}

	fun createBattlefield(battlefield: Battlefield): String {

		val boundaries = battlefield.boundaries

		val topBorder = "┏" + "-".repeat(boundaries.width) + "┓\n"
		val emptyRow = "┃" + " ".repeat(boundaries.width) + "┃\n"
		val bottomBorder = "┗" + "-".repeat(boundaries.width) + "┛\n"

		val rows = (1 .. boundaries.height).map { emptyRow }.joinToString("")

		return topBorder + rows + bottomBorder
	}

	private fun displaySoldiersInBattlefield(
		battlefieldTxt: String,
		battlefieldBoundaries: BattlefieldBoundaries,
		soldiersBattlePositions: SoldiersBattlePositions): String {

		val battleTxt = StringBuilder(battlefieldTxt)
		soldiersBattlePositions.forEach({
			soldierAndPosition -> displaySoldierInBattlefield(
			battleTxt, battlefieldBoundaries, soldierAndPosition.second)
		})
		return battleTxt.toString()
	}

	private fun displaySoldierInBattlefield(
		battleTxt: StringBuilder,
		battlefieldBoundaries: BattlefieldBoundaries,
		position: BattlefieldPosition) {

		val positionInTxt = toPositionInText(position, battlefieldBoundaries)
		battleTxt.replace(positionInTxt, positionInTxt+1, charForSoldier().toString())
	}

	private fun toPositionInText(position: BattlefieldPosition, battlefieldBoundaries: BattlefieldBoundaries): Int {
		return (position.y * (battlefieldBoundaries.width+2+1)) + position.x
	}

	private fun charForSoldier(): Char = 'S'
}