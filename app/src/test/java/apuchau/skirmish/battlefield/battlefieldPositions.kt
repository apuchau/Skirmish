package apuchau.skirmish.battlefield

import com.natpryce.onError

fun battlefieldPosition(x: Int, y: Int) =
	BattlefieldPosition.create(x,y).onError { throw Exception("Unexpected error preparing data for test") }

