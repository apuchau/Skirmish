package apuchau.skirmish.battle

import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus

data class SoldierInBattle(val soldier: Soldier,
									val position: BattlefieldPosition,
									val status: SoldierStatus,
									val action: SoldierAction)
