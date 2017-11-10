package apuchau.skirmish.battle

import apuchau.kotlin.result.assertError
import apuchau.skirmish.battlefield.battlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import org.junit.Test

class SoldiersBattlePositionsTests {

	@Test
	fun if_two_soldiers_in_the_same_initial_position_then_error() {

		assertError(
			SoldiersBattlePositions.create(listOf(
				Pair(Soldier(SoldierId("SoldierA")), battlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB")), battlefieldPosition(1,1))
			)),
			"Some soldiers occupy the same battlefield spot")
	}

	@Test
	fun if_any_soldier_duplicate_then_error() {

		assertError(
			SoldiersBattlePositions.create(listOf(
				Pair(Soldier(SoldierId("SoldierA")), battlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierA")), battlefieldPosition(2,1))
			)),
			"There are duplicated soldiers in the battle")
	}
}