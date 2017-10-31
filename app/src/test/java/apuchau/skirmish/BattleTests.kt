package apuchau.skirmish

import apuchau.skirmish.exception.NotEnoughSoldiers
import apuchau.skirmish.exception.InvalidSoldiersPosition
import apuchau.skirmish.exception.SoldierNotInArmy
import org.junit.Test
import kotlin.test.assertFailsWith

class BattleTests {

	@Test
	fun you_need_at_least_two_armies_to_fight() {

		val battlefield = Battlefield(BattlefieldBoundaries(2,1))

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1"))))
		)

		assertFailsWith(NotEnoughSoldiers::class) {
			Battle(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), BattlefieldPosition(1,1))
			)))
		}
	}

	@Test
	fun all_soldiers_in_battlefield_belongs_to_armies() {

		val battlefield = Battlefield(BattlefieldBoundaries(3,1))

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertFailsWith(SoldierNotInArmy::class) {
			Battle(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), BattlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB1")), BattlefieldPosition(2,1)),
				Pair(Soldier(SoldierId("SoldierC1")), BattlefieldPosition(3,1))
			)))
		}
	}

	@Test
	fun you_need_at_least_one_soldier_for_each_army_positioned_in_battlefield() {

		val battlefield = Battlefield(BattlefieldBoundaries(2,1))

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertFailsWith(NotEnoughSoldiers::class) {
			Battle(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), BattlefieldPosition(1,1))
			)))
		}

		assertFailsWith(NotEnoughSoldiers::class) {
			Battle(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierB1")), BattlefieldPosition(1,1))
			)))
		}
	}

	@Test
	fun if_soldier_position_out_of_battlefield_bounds_throw_exception() {

		val battlefield = Battlefield(BattlefieldBoundaries(2,1))

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertFailsWith(InvalidSoldiersPosition::class) {
			Battle(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), BattlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB1")), BattlefieldPosition(3,1))
			)))
		}
	}


	@Test
	fun soldier_can_be_positioned_in_battlefield_border__no_error_thrown() {

		val battlefield = Battlefield(BattlefieldBoundaries(3,5))

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		Battle(battlefield, armies, SoldiersBattlePositions(listOf(
			Pair(Soldier(SoldierId("SoldierA1")), BattlefieldPosition(3,1)),
			Pair(Soldier(SoldierId("SoldierB1")), BattlefieldPosition(1,5))
		)))
	}
}