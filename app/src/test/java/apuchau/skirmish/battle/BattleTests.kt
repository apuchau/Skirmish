package apuchau.skirmish.battle

import apuchau.skirmish.army.Army
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.battlefieldPosition
import apuchau.skirmish.exception.InvalidSoldiersPosition
import apuchau.skirmish.exception.NotEnoughSoldiers
import apuchau.skirmish.exception.SoldierNotInArmy
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import com.natpryce.onError
import org.junit.Test
import kotlin.test.assertFailsWith

class BattleTests {

	@Test
	fun you_need_at_least_two_armies_to_fight() {

		val battlefield = createBattlefield(2,1)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1"))))
		)

		assertFailsWith(NotEnoughSoldiers::class) {
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(1,1))
			)))
		}
	}

	@Test
	fun all_soldiers_in_battlefield_belongs_to_armies() {

		val battlefield = createBattlefield(3,1)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertFailsWith(SoldierNotInArmy::class) {
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB1")), battlefieldPosition(2,1)),
				Pair(Soldier(SoldierId("SoldierC1")), battlefieldPosition(3,1))
			)))
		}
	}

	@Test
	fun you_need_at_least_one_soldier_for_each_army_positioned_in_battlefield() {

		val battlefield = createBattlefield(2,1)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertFailsWith(NotEnoughSoldiers::class) {
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(1,1))
			)))
		}

		assertFailsWith(NotEnoughSoldiers::class) {
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierB1")), battlefieldPosition(1,1))
			)))
		}
	}

	@Test
	fun if_soldier_position_out_of_battlefield_bounds_throw_exception() {

		val battlefield = createBattlefield(2,1)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertFailsWith(InvalidSoldiersPosition::class) {
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB1")), battlefieldPosition(3,1))
			)))
		}
	}


	@Test
	fun soldier_can_be_positioned_in_battlefield_edge__no_error_thrown() {

		val battlefield = createBattlefield(3,5)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
			Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(3,1)),
			Pair(Soldier(SoldierId("SoldierB1")), battlefieldPosition(1,5))
		)))
	}


	private fun createBattlefield(width: Int, height: Int): Battlefield {
		return Battlefield(
			BattlefieldBoundaries.create(width, height)
				.onError { throw Exception("Invalid boundaries. ${it.reason}") })
	}
}