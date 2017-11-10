package apuchau.skirmish.app

import apuchau.skirmish.battlefield.Battlefield

interface BattleView {
	fun displayBattleStatus(battlefield: Battlefield)
}