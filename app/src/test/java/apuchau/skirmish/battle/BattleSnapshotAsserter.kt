package apuchau.skirmish.battle

import kotlin.test.assertTrue

class BattleSnapshotAsserter(val snapshot: BattleSnapshot) {

	fun assertAllSoldiersIdle() {
		assertTrue(
			snapshot.soldiersStatuses
				.map { armyToSoldierStatuses -> armyToSoldierStatuses.value }
				.flatMap { soldiersToStatuses -> soldiersToStatuses.values }
				.all { status -> status == SoldierStatus.DOING_NOTHING }
		)
	}
}