package apuchau.skirmish.battle

import apuchau.skirmish.soldier.Soldier

class ActionCalculationResult(
	val soldier: Soldier,
	val currentAction: SoldierAction,
	val newAction: SoldierAction)