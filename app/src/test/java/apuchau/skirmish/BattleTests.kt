package apuchau.skirmish

import org.junit.Test
import kotlin.test.assertFailsWith

class BattleTests {

	@Test
	fun if_two_soldiers_in_the_same_initial_position_throw_exception() {

		val battlefield = Battlefield(2, 1)
		val soldiersPossitioned = listOf(
			Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,1)),
			Pair(Soldier(SoldierId("SoldierB")), BattlefieldPosition(1,1))
		)

		assertFailsWith(InvalidSoldiersPosition::class) {
			Battle(battlefield, soldiersPossitioned)
		}
	}

	@Test
	fun if_any_soldier_duplicate_throw_exception() {

		val battlefield = Battlefield(2, 1)
		val soldiersPossitioned = listOf(
			Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,1)),
			Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(2,1))
		)

		assertFailsWith(DuplicatedSoldier::class) {
			Battle(battlefield, soldiersPossitioned)
		}
	}

	@Test
	fun if_no_soldiers_initially_in_battle_throw_exception() {

		val battlefield = Battlefield(2, 1)
		val soldiersPossitioned = emptyList<Pair<Soldier,BattlefieldPosition>>()

		assertFailsWith(BattleWithNoSoldiers::class) {
			Battle(battlefield, soldiersPossitioned)
		}
	}
}