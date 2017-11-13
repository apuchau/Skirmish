package apuchau.skirmish.army

import apuchau.kotlin.result.assertError
import org.junit.Test

class ArmyTests {

	@Test
	fun if_army_has_no_soldiers__then_error() {

		assertError(Army.create("Army A", emptySet()),
			"You need at least one soldier in an army")
	}
}
