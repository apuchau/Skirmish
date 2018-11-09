package apuchau.skirmish.battle

import apuchau.skirmish.battle.log.BattleLog
import apuchau.skirmish.battlefield.Battlefield

data class BattleSnapshot(val battlefield: Battlefield,
								  val soldiersInBattle: SoldiersInBattle,
								  val battleLog : BattleLog)


