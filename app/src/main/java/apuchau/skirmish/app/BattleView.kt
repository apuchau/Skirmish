package apuchau.skirmish.app

import apuchau.skirmish.battle.BattleSnapshot

interface BattleView {
	fun displayBattleSnapshot(snapshot: BattleSnapshot)
}