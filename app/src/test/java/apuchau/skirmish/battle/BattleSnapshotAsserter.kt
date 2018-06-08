package apuchau.skirmish.battle

import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BattleSnapshotAsserter(val snapshot: BattleSnapshot) {

	fun assertAllSoldiersIdle() =
		assertAllSoldiersDoing(SoldierAction.DO_NOTHING)

	fun assertAllSoldiersBattling() =
		assertAllSoldiersDoing(SoldierAction.FIGHT)

	private fun assertAllSoldiersDoing(expectedAction: SoldierAction): BattleSnapshotAsserter {
		assertTrue(snapshot.soldiersActions.areAllSoldiersDoing(expectedAction))
		return this
	}

	fun assertAllSoldiersHealthy() : BattleSnapshotAsserter {
		assertTrue(snapshot.soldiersStatuses.areAllSoldiersHealthy())
		return this
	}

	fun assertAllSoldiersDead(): BattleSnapshotAsserter {
		assertTrue(snapshot.soldiersStatuses.areAllSoldiersDead())
		return this
	}

	fun assertSoldierIsWounded(soldier: Soldier): BattleSnapshotAsserter {
		assertEquals(snapshot.soldiersStatuses.soldierStatus(soldier), SoldierStatus.WOUNDED)
		return this
	}

	fun assertLog(expectedEntries: List<String>) {
		assertEquals( expectedEntries, snapshot.battleLog.entries(), "Log entries")
	}
}