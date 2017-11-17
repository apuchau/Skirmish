package apuchau.skirmish.battle

import kotlin.test.assertTrue

class BattleSnapshotAsserter(val snapshot: BattleSnapshot) {

	fun assertAllSoldiersIdle() {
		assertAllSoldiersDoing(SoldierAction.DO_NOTHING)
	}

	fun assertAllSoldiersBattling() {
		assertAllSoldiersDoing(SoldierAction.FIGHT)
	}

	private fun assertAllSoldiersDoing(expectedAction: SoldierAction) =
		assertTrue(snapshot.soldiersActions.areAllSoldiersDoing(expectedAction))
}