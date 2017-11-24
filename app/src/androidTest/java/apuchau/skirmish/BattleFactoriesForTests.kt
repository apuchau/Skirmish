package apuchau.skirmish

import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition
import com.natpryce.onError

fun battlefield(width: Int, height: Int): Battlefield {
	return Battlefield(
		BattlefieldBoundaries.create(width, height)
			.onError { throw Exception("Invalid boundaries. ${it.reason}") })
}

fun battlefieldPosition(x: Int, y: Int): BattlefieldPosition =
	BattlefieldPosition.create(x,y).onError { throw Exception("Error creating battlefield position. ${it.reason}") }
