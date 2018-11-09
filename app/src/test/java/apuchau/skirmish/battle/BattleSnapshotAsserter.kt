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

		assertTrue(snapshot.soldiersInBattle.soldiersActions.areAllSoldiersDoing(expectedAction))
		return this
	}

	fun assertAllSoldiersHealthy() : BattleSnapshotAsserter {
		assertTrue(snapshot.soldiersInBattle.soldiersStatuses.areAllSoldiersHealthy())
		return this
	}

	fun assertSoldierIsWounded(soldier: Soldier): BattleSnapshotAsserter {
		assertEquals(snapshot.soldiersInBattle.soldiersStatuses.soldierStatus(soldier), SoldierStatus.WOUNDED)
		return this
	}

	fun assertSoldierIsDead(soldier: Soldier): BattleSnapshotAsserter {
		assertEquals(snapshot.soldiersInBattle.soldiersStatuses.soldierStatus(soldier), SoldierStatus.DEAD)
		return this
	}

	fun assertLog(expectedEntries: List<String>) {
		assertEquals( expectedEntries, snapshot.battleLog.entries(), "Log entries")
	}
}