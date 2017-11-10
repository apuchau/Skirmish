package apuchau.skirmish.app

import apuchau.skirmish.Battlefield

interface BattleView {
	fun displayBattleStatus(battlefield: Battlefield)
}