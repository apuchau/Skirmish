package apuchau.skirmish.app.text

import apuchau.skirmish.battle.BattleSnapshot
import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battle.SoldiersStatuses
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition

fun textRepresentation(battleSnapshot: BattleSnapshot) : String {

		val battlefieldTxt = createBattlefield(battleSnapshot.battlefield)
		return displaySoldiersInBattlefield(
			battlefieldTxt,
			battleSnapshot.battlefield.boundaries,
			battleSnapshot.soldiersBattlePositions,
			battleSnapshot.soldiersStatuses)
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
	soldiersBattlePositions: SoldiersBattlePositions,
	soldiersStatuses: SoldiersStatuses): String {

	val battleTxt = StringBuilder(battlefieldTxt)

	soldiersStatuses
		.soldiersAlive()
		.mapNotNull { soldier -> soldiersBattlePositions.soldierPosition(soldier)?.let { Pair(soldier, it) }}
		.forEach { displaySoldierInBattlefield(battleTxt, battlefieldBoundaries, it.second)}

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
