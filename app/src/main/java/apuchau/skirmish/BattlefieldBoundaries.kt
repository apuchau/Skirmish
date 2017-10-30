package apuchau.skirmish

import apuchau.skirmish.exception.InvalidBoundaryValue

data class BattlefieldBoundaries(val width: Int, val height: Int) {
	init {
		checkPositive(width, "width")
		checkPositive(height, "height")
	}

	private fun checkPositive(value: Int, name: String) {
		if (value <= 0) {
			throw InvalidBoundaryValue("$value is not value, boundary '$name' must be bigger than zero")
		}
	}
}