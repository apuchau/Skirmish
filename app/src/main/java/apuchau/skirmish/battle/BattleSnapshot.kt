package apuchau.skirmish.battle

import apuchau.skirmish.army.Army
import apuchau.skirmish.battlefield.Battlefield
import apuchau.skirmish.soldier.Soldier

data class BattleSnapshot(val battlefield: Battlefield,
								  val soldiersBattlePositions: SoldiersBattlePositions,
								  val soldiersStatuses: Map<Army, Map<Soldier, SoldierStatus>>)