package apuchau.skirmish.battle

import apuchau.kotlin.result.assertError
import apuchau.skirmish.army.Army
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.battlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import com.natpryce.onError
import org.junit.Test

class BattleTests {

	@Test
	fun you_need_at_least_two_armies_to_fight() {

		val battlefield = createBattlefield(2,1)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1"))))
		)

		assertError(
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(1,1))
			))),
			"You need at least two armies to battle"
		)
	}

	@Test
	fun all_soldiers_in_battlefield_belongs_to_armies() {

		val battlefield = createBattlefield(3,1)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertError(
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB1")), battlefieldPosition(2,1)),
				Pair(Soldier(SoldierId("SoldierC1")), battlefieldPosition(3,1))
			))),
			"Not all soldiers in the battlefield belong to armies")
	}

	@Test
	fun you_need_at_least_one_soldier_for_each_army_positioned_in_battlefield() {

		val battlefield = createBattlefield(2,1)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertError (
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(1,1))
			))),
			"Not all armies are represented in the battlefield"
		)

		assertError(
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierB1")), battlefieldPosition(1,1))
			))),
			"Not all armies are represented in the battlefield"
		)
	}

	@Test
	fun if_soldier_position_out_of_battlefield_bounds_throw_exception() {

		val battlefield = createBattlefield(2,1)

		val armies = setOf(
			Army("Army A", setOf(Soldier(SoldierId("SoldierA1")))),
			Army("Army B", setOf(Soldier(SoldierId("SoldierB1"))))
		)

		assertError(
			Battle.instance(battlefield, armies, SoldiersBattlePositions(listOf(
				Pair(Soldier(SoldierId("SoldierA1")), battlefieldPosition(1,1)),
				Pair(Soldier(SoldierId("SoldierB1")), battlefieldPosition(3,1))
			))),
			"Some positions are out of the battlefield bounds"
		)
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