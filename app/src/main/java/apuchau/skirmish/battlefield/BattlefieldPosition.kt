package apuchau.skirmish.battlefield

import com.natpryce.*

class BattlefieldPosition private constructor(val x: Int, val y: Int) {

	companion object Factory {

		fun create(x: Int, y: Int) : Result<BattlefieldPosition, String> {

			return checkPositive(x, "x")
				.flatMap { checkPositive(y, "y") }
				.map { BattlefieldPosition(x,y) }
		}

		private fun checkPositive(value: Int, name: String) : Result<Unit,String> {
			return if (value > 0)
				Ok(Unit)
			else
				Err("value $value for '$name' is not valid, must be greater than zero")
		}
	}

	fun isWithinBounds(battlefieldBoundaries: BattlefieldBoundaries): Boolean =
		x <= battlefieldBoundaries.width
			&& y <= battlefieldBoundaries.height

	override fun hashCode(): Int = 31 * x + y

	override fun equals(other: Any?): Boolean =
			other != null
		&& other is BattlefieldPosition
		&& this.x == other.x
		&& this.y == other.y

	override fun toString(): String {
		return "Position($x,$y)"
	}
}