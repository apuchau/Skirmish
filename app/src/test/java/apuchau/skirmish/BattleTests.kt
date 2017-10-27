package apuchau.skirmish

import org.junit.Test
import kotlin.test.assertFailsWith

class BattleTests {

	@Test
	fun soldiers_cannot_be_in_the_same_initial_position() {
		val battlefield = Battlefield(3, 1)
		val soldiersPossitioned = listOf(
			Pair(Soldier(), BattlefieldPosition(1,1)),
			Pair(Soldier(), BattlefieldPosition(1,1))
		)

		assertFailsWith(InvalidSoldiersPosition::class) {
			Battle(battlefield, soldiersPossitioned)
		}
	}
}