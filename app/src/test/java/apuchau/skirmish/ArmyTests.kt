package apuchau.skirmish

import apuchau.skirmish.exception.NotEnoughSoldiers
import org.junit.Test
import kotlin.test.assertFailsWith

class ArmyTests {

	@Test
	fun if_army_has_no_soldiers__throw_exception() {

		assertFailsWith(NotEnoughSoldiers::class) {
			Army("Army A", emptySet())
		}
	}
}
