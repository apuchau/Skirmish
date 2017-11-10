package apuchau.skirmish.battlefield

import apuchau.kotlin.result.assertError
import org.junit.Test

class BattlefieldPositionTests {

	@Test
	fun if_x_less_than_zero_then_error() {
		assertError(
			BattlefieldPosition.create(-1,1), "value -1 for 'x' is not valid, must be greater than zero"
		)
	}

	@Test
	fun if_x_is_zero_then_error() {
		assertError(
			BattlefieldPosition.create(0,1), "value 0 for 'x' is not valid, must be greater than zero"
		)
	}

	@Test
	fun if_y_less_than_zero_then_error() {
		assertError(
			BattlefieldPosition.create(1,-1), "value -1 for 'y' is not valid, must be greater than zero"
		)
	}

	@Test
	fun if_y_is_zero_then_error() {
		assertError(
			BattlefieldPosition.create(1,0), "value 0 for 'y' is not valid, must be greater than zero"
		)
	}
}