package apuchau.skirmish.app.text

import apuchau.skirmish.battle.BattleSnapshot
import apuchau.skirmish.battle.SoldierInBattle
import apuchau.skirmish.battle.SoldiersInBattle
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition

fun textRepresentation(battleSnapshot: BattleSnapshot) : String {

		val battlefieldTxt = createBattlefield(battleSnapshot.battlefield)
		return displaySoldiersInBattlefield(
			battlefieldTxt,
			battleSnapshot.battlefield.boundaries,
			battleSnapshot.soldiersInBattle)
}

fun createBattlefield(battlefield: Battlefield): String {

	val boundaries = battlefield.boundaries

	val topBorder = "┏" + "━".repeat(boundaries.width) + "┓\n"
	val emptyRow = "┃" + " ".repeat(boundaries.width) + "┃\n"
	val bottomBorder = "┗" + "━".repeat(boundaries.width) + "┛\n"

	val rows = (1 .. boundaries.height).map { emptyRow }.joinToString("")

	return topBorder + rows + bottomBorder
}

private fun displaySoldiersInBattlefield(
	battlefieldTxt: String,
	battlefieldBoundaries: BattlefieldBoundaries,
	soldiersInBattle: SoldiersInBattle): String {

	val battleTxt = StringBuilder(battlefieldTxt)

	soldiersInBattle
		.alive()
		.forEach { soldierInBattle -> displaySoldierInBattlefield(battleTxt, battlefieldBoundaries, soldierInBattle)}

	return battleTxt.toString()
}

private fun displaySoldierInBattlefield(
	battleTxt: StringBuilder,
	battlefieldBoundaries: BattlefieldBoundaries,
	soldierInBattle: SoldierInBattle) {

	val positionInTxt = toPositionInText(soldierInBattle.position, battlefieldBoundaries)
	battleTxt.replace(positionInTxt, positionInTxt+1, charForSoldier().toString())
}

private fun toPositionInText(position: BattlefieldPosition, battlefieldBoundaries: BattlefieldBoundaries): Int {
	return (position.y * (battlefieldBoundaries.width+2+1)) + position.x
}

private fun charForSoldier(): Char = 'S'
