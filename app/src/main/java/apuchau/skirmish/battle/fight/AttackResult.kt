package apuchau.skirmish.battle.fight

import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus

data class AttackResult(val attacker: Soldier, val defender: Soldier, val newDefenderStatus: SoldierStatus)