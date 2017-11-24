package apuchau.skirmish.app

import apuchau.skirmish.battle.BattleSnapshot

interface BattlefieldView {
	fun displayBattleSnapshot(snapshot: BattleSnapshot)
}