package apuchau.skirmish.battle

import apuchau.kotlin.result.assertError
import apuchau.skirmish.army.Army
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.battlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierId
import com.natpryce.onError
import com.natpryce.valueOrNull
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

private val soldierA1 = Soldier(SoldierId("SoldierA1"))
private val soldierB1 = Soldier(SoldierId("SoldierB1"))
private val soldierC1 = Soldier(SoldierId("SoldierC1"))

class BattleTests {

	@Test
	fun you_need_at_least_two_armies_to_fight() {

		val battlefield = createBattlefield(2,1)

		val armies = setOf(
			createArmy("Army A", setOf(soldierA1))
		)

		val soldierA1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierA1, battlefieldPosition(1, 1))

		assertError(
			Battle.instance(battlefield, armies, listOf(soldierA1InBattle)),
			"Not enough armies to battle"
		)
	}

	@Test
	fun all_soldiers_in_battlefield_belongs_to_armies() {

		val battlefield = createBattlefield(3, 1)

		val armies = setOf(
			createArmy("Army A", setOf(soldierA1)),
			createArmy("Army B", setOf(soldierB1))
		)

		val soldierA1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierA1, battlefieldPosition(1, 1))
		val soldierB1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierB1, battlefieldPosition(2, 1))
		val soldierC1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierC1, battlefieldPosition(3, 1))

		assertError(
			Battle.instance(battlefield, armies, listOf(soldierA1InBattle, soldierB1InBattle, soldierC1InBattle)),
			"Not all soldiers in the battlefield belong to armies")
	}

	@Test
	fun you_need_at_least_one_soldier_for_each_army_positioned_in_battlefield() {

		val battlefield = createBattlefield(2, 1)

		val armies = setOf(
			createArmy("Army A", setOf(soldierA1)),
			createArmy("Army B", setOf(soldierB1))
		)

		val soldierA1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierA1, battlefieldPosition(1, 1))
		val soldierB1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierB1, battlefieldPosition(2, 1))

		assertError(
			Battle.instance(battlefield, armies, listOf(soldierA1InBattle)),
			"Not all armies are represented in the battlefield"
		)

		assertError(
			Battle.instance(battlefield, armies, listOf(soldierB1InBattle)),
			"Not all armies are represented in the battlefield"
		)
	}

	@Test
	fun if_soldier_position_out_of_battlefield_bounds_throw_exception() {

		val battlefield = createBattlefield(2, 1)

		val armies = setOf(
			createArmy("Army A", setOf(soldierA1)),
			createArmy("Army B", setOf(soldierB1))
		)

		val soldierA1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierA1, battlefieldPosition(1, 1))
		val soldierB1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierB1, battlefieldPosition(3, 1))

		assertError(
			Battle.instance(battlefield, armies, listOf(soldierA1InBattle, soldierB1InBattle)),
			"Some positions are out of the battlefield bounds"
		)
	}

	@Test
	fun soldier_can_be_positioned_in_battlefield_edge__no_error_thrown() {

		val battlefield = createBattlefield(3, 5)

		val armies = setOf(
			createArmy("Army A", setOf(soldierA1)),
			createArmy("Army B", setOf(soldierB1))
		)

		val soldierA1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierA1, battlefieldPosition(3, 1))
		val soldierB1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierB1, battlefieldPosition(1, 5))

		val result = Battle.instance(battlefield, armies, listOf(soldierA1InBattle, soldierB1InBattle))

		result.onError { fail("Battle with soldiers in battlefield edge is allowed, but has failed creation") }

		val soldiersInBattle = result.valueOrNull()?.snapshot()!!.soldiersInBattle

		assertEquals(battlefieldPosition(3, 1), soldiersInBattle.soldierPosition(soldierA1))
		assertEquals(battlefieldPosition(1, 5), soldiersInBattle.soldierPosition(soldierB1))
	}

	@Test
	fun `if two soldiers in the same initial position then error`() {

		val battlefield = createBattlefield(3, 5)

		val armies = setOf(
			createArmy("Army A", setOf(soldierA1)),
			createArmy("Army B", setOf(soldierB1))
		)

		val soldierA1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierA1, battlefieldPosition(1, 1))
		val soldierB1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierB1, battlefieldPosition(1, 1))

		val result = Battle.instance(battlefield, armies, listOf(soldierA1InBattle, soldierB1InBattle))

		assertError(result, "Some soldiers are in the same position")
	}

	@Test
	fun if_any_soldier_duplicate_then_error() {

		val battlefield = createBattlefield(3, 5)

		val armies = setOf(
			createArmy("Army A", setOf(soldierA1)),
			createArmy("Army B", setOf(soldierB1))
		)

		val soldierA1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierA1, battlefieldPosition(1, 1))
		val soldierA1duplicatedInBattle = SoldierInBattle.createHealthyDoingNothing(soldierA1, battlefieldPosition(2, 1))
		val soldierB1InBattle = SoldierInBattle.createHealthyDoingNothing(soldierB1, battlefieldPosition(3, 1))

		val result = Battle.instance(battlefield, armies,
			listOf(soldierA1InBattle, soldierA1duplicatedInBattle, soldierB1InBattle))

		assertError(result, "Some soldiers are duplicated")
	}


	private fun createBattlefield(width: Int, height: Int): Battlefield =
		Battlefield(
			BattlefieldBoundaries.create(width, height)
				.onError { throw Exception("Can't create battlefield. ${it.reason}") })

	private fun createArmy(armyId: String, soldiers: Set<Soldier>): Army =
		Army.create(armyId, soldiers)
			.onError { throw Exception("Can't create army: ${it.reason} ") }
}