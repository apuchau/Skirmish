package apuchau.skirmish

import org.junit.Test
import kotlin.test.assertFailsWith

class BattleTests {

	@Test
	fun soldiers_cannot_be_in_the_same_initial_position() {

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
	fun only_one_instance_of_each_soldier() {

		val battlefield = Battlefield(2, 1)
		val soldiersPossitioned = listOf(
			Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(1,1)),
			Pair(Soldier(SoldierId("SoldierA")), BattlefieldPosition(2,1))
		)

		assertFailsWith(DuplicatedSoldier::class) {
			Battle(battlefield, soldiersPossitioned)
		}
	}
}