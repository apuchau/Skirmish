package apuchau.skirmish.battle

import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus

data class SoldierInBattle(val soldier: Soldier,
									val position: BattlefieldPosition,
									val status: SoldierStatus,
									val action: SoldierAction) {

	companion object Factory {
		fun createHealthyDoingNothing(soldier: Soldier, position: BattlefieldPosition): SoldierInBattle =
			SoldierInBattle(soldier, position, SoldierStatus.HEALTHY, SoldierAction.DO_NOTHING)
	}

	fun isAlive() = !isDead()
	fun isDead() = status == SoldierStatus.DEAD

	fun adjacentPositions(): Collection<BattlefieldPosition> =
		position.adjacentPositions()

}
