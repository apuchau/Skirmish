package apuchau.skirmish.battle

import apuchau.skirmish.battle.log.BattleLog
import apuchau.skirmish.battlefield.Battlefield

data class BattleSnapshot(val battlefield: Battlefield,
								  val soldiersStatuses: SoldiersStatuses,
								  val soldiersBattlePositions: SoldiersBattlePositions,
								  val soldiersActions: SoldiersBattleActions,
								  val battleLog : BattleLog)


