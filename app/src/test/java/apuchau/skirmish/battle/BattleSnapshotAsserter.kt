package apuchau.skirmish.battle

import kotlin.test.assertTrue

class BattleSnapshotAsserter(val snapshot: BattleSnapshot) {

	fun assertAllSoldiersIdle() {
		assertAllSoldiersInStatus(SoldierStatus.DOING_NOTHING)
	}

	fun assertAllSoldiersBattling() {
		assertAllSoldiersInStatus(SoldierStatus.FIGHTING)
	}

	private fun assertAllSoldiersInStatus( expectedStatus : SoldierStatus ) {
		assertTrue(
			snapshot.soldiersStatuses
				.map { armyToSoldierStatuses -> armyToSoldierStatuses.value }
				.flatMap { soldiersToStatuses -> soldiersToStatuses.values }
				.all { status -> status == expectedStatus }
		)
	}
}