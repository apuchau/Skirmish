package apuchau.skirmish.battle

import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier

// TODO - Refactor this to use SoldierInBattle object
data class SoldiersInBattle(
	val soldiersStatuses: SoldiersStatuses,
	val soldiersBattlePositions: SoldiersBattlePositions,
	val soldiersActions: SoldiersBattleActions) {

	fun alive(): List<SoldierInBattle> =
		soldiersStatuses
			.soldiersAlive()
			.map { soldier -> SoldierInBattle(soldier,
				soldiersBattlePositions.soldierPosition(soldier)!!,
				soldiersStatuses.soldierStatus(soldier)!!,
				soldiersActions.actionForSoldier(soldier)!!)
			}

	fun soldierPosition(soldier: Soldier): BattlefieldPosition? =
		soldiersBattlePositions.soldierPosition(soldier)

}