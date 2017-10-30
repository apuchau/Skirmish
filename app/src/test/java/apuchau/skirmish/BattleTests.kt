package apuchau.skirmish

import apuchau.skirmish.exception.BattleWithNoSoldiers
import apuchau.skirmish.exception.InvalidSoldiersPosition
import org.junit.Test
import kotlin.test.assertFailsWith

class BattleTests {

	@Test
	fun if_no_soldiers_initially_in_battle_throw_exception() {

		val battlefield = Battlefield(BattlefieldBoundaries(2,1))

		assertFailsWith(BattleWithNoSoldiers::class) {
			Battle(battlefield, SoldiersBattlePositions(emptyList()))
		}
	}

	@Test
	fun if_soldier_position_out_of_battlefield_bounds_throw_exception() {

		val battlefield = Battlefield(BattlefieldBoundaries(2,1))

		assertFailsWith(InvalidSoldiersPosition::class) {
			Battle(battlefield, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB")), BattlefieldPosition(3,1))
			)))
		}
	}


}