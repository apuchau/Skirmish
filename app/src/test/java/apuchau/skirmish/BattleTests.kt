package apuchau.skirmish

import apuchau.skirmish.exception.BattleWithNoSoldiers
import apuchau.skirmish.exception.DuplicatedSoldier
import apuchau.skirmish.exception.InvalidSoldiersPosition
import org.junit.Test
import kotlin.test.assertFailsWith

class BattleTests {

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

	@Test
	fun if_no_soldiers_initially_in_battle_throw_exception() {

		val battlefield = Battlefield(2, 1)

		assertFailsWith(BattleWithNoSoldiers::class) {
			Battle(battlefield, SoldiersBattlePositions(emptyList()))
		}
	}


}