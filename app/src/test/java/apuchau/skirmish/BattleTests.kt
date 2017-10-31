package apuchau.skirmish

import apuchau.skirmish.exception.NotEnoughSoldiersToBattle
import apuchau.skirmish.exception.InvalidSoldiersPosition
import org.junit.Test
import kotlin.test.assertFailsWith

class BattleTests {

	@Test
	fun you_need_at_least_two_soldiers_to_fight_a_battle() {

		val battlefield = Battlefield(BattlefieldBoundaries(2,1))

		assertFailsWith(NotEnoughSoldiersToBattle::class) {
			Battle(battlefield, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,1))
			)))
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