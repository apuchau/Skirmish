package apuchau.skirmish.battlefield

import apuchau.skirmish.exception.InvalidSoldiersPosition

data class BattlefieldPosition(val x: Int, val y: Int) {

	init {
		checkPositive(x, "x")
		checkPositive(y, "y")
	}

	private fun checkPositive(value: Int, name: String) {
		if (value <= 0) {
			throw InvalidSoldiersPosition("$value is not value, '$name' must be bigger than zero")
		}
	}

	fun isWithinBounds(battlefieldBoundaries: BattlefieldBoundaries): Boolean =
		x <= battlefieldBoundaries.width
			&& y <= battlefieldBoundaries.height
}