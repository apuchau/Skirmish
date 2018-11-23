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

		assertTrue(snapshot.soldiersInBattle
			.allSoldiers()
			.all{ snapshot.soldiersInBattle.actionForSoldier(it) == expectedAction })
		return this
	}

	fun assertAllSoldiersHealthy() : BattleSnapshotAsserter {
		assertTrue(snapshot.soldiersInBattle
			.allSoldiers()
			.all{ snapshot.soldiersInBattle.statusForSoldier(it) == SoldierStatus.HEALTHY })
		return this
	}

	fun assertSoldierIsWounded(soldier: Soldier): BattleSnapshotAsserter {
		assertEquals(SoldierStatus.WOUNDED, snapshot.soldiersInBattle.statusForSoldier(soldier))
		return this
	}

	fun assertSoldierIsDead(soldier: Soldier): BattleSnapshotAsserter {
		assertEquals(SoldierStatus.DEAD, snapshot.soldiersInBattle.statusForSoldier(soldier))
		return this
	}

	fun assertLog(expectedEntries: List<String>) {
		assertEquals( expectedEntries, snapshot.battleLog.entries(), "Log entries")
	}
}