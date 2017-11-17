package apuchau.skirmish.battle

import apuchau.skirmish.battlefield.Battlefield

data class BattleSnapshot(val battlefield: Battlefield,
								  val soldiersBattlePositions: SoldiersBattlePositions,
								  val soldiersActions: SoldiersBattleActions)
