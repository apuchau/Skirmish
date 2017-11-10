package apuchau.skirmish

import apuchau.skirmish.battle.SoldiersBattlePositions
import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.exception.DuplicatedSoldier
import apuchau.skirmish.exception.InvalidSoldiersPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import org.junit.Test
import kotlin.test.assertFailsWith

class SoldiersBattlePositionsTests {

	@Test
	fun if_x_negative_throw_exception() {

		assertFailsWith(InvalidSoldiersPosition::class) {
			SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(-1,1))
			))
		}
	}

	@Test
	fun if_x_zero_throw_exception() {

		assertFailsWith(InvalidSoldiersPosition::class) {
			SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(0,1))
			))
		}
	}

	@Test
	fun if_y_negative_throw_exception() {

		assertFailsWith(InvalidSoldiersPosition::class) {
			SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,-1))
			))
		}
	}

	@Test
	fun if_y_zero_throw_exception() {

		assertFailsWith(InvalidSoldiersPosition::class) {
			SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,-1))
			))
		}
	}


	@Test
	fun if_two_soldiers_in_the_same_initial_position_throw_exception() {

		assertFailsWith(InvalidSoldiersPosition::class) {
			SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB")), BattlefieldPosition(1,1))
			))
		}
	}

	@Test
	fun if_any_soldier_duplicate_throw_exception() {

		assertFailsWith(DuplicatedSoldier::class) {
			SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(2,1))
			))
		}
	}
}