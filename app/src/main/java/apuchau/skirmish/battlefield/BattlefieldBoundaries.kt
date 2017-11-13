package apuchau.skirmish.battlefield

import com.natpryce.*


class BattlefieldBoundaries private constructor(val width: Int, val height: Int) {

	companion object Factory {

		fun create(width: Int, height: Int) : Result<BattlefieldBoundaries, String> {

			return checkPositive(width, "width")
				.flatMap { checkPositive(height, "height") }
				.map { BattlefieldBoundaries(width, height) }
		}

		private fun checkPositive(value: Int, name: String) : Result<Unit,String> {
			return if (value > 0)
				Ok(Unit)
			else
				Err("$value is not value, boundary '$name' must be bigger than zero")
		}
	}

	override fun hashCode(): Int = 31 * width + height

	override fun equals(other: Any?): Boolean =
		other != null
			&& other is BattlefieldBoundaries
			&& this.width == other.width
			&& this.height == other.height

	override fun toString(): String {
		return "Boundaries($width,$height)"
	}
}