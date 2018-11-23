package apuchau.skirmish.battle

import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus

data class SoldiersInBattle(val soldiersInBattle : Collection<SoldierInBattle>) {

	// TODO - Does this class make sense? (just wrapping the collection)

	fun alive(): List<SoldierInBattle> =
		soldiersInBattle.filter { soldierInBattle -> soldierInBattle.isAlive() }

	fun soldierPosition(soldier: Soldier): BattlefieldPosition? =
		findSoldierInBattle(soldier)
			.let { it?.position }

	fun applyNewSoldierActions(soldiersNewActions: Collection<Pair<Soldier, SoldierAction>>): SoldiersInBattle =
		SoldiersInBattle(
			soldiersInBattle.map {
				val newActionMaybe = soldiersNewActions.find { soldierAndAction -> soldierAndAction.first.equals(it.soldier) }?.second
				SoldierInBattle(it.soldier, it.position, it.status, newActionMaybe ?: it.action)
			}
		)

	fun actionForSoldier(soldier: Soldier): SoldierAction? = findSoldierInBattle(soldier)?.action

	fun soldiersAdjacentToSoldier(soldier: Soldier): Collection<SoldierInBattle> =
		findSoldierInBattle(soldier)?.let { soldiersAdjacentTo(it.position) }?: emptySet()

	private fun soldiersAdjacentTo(position: BattlefieldPosition): Collection<SoldierInBattle> =
		position.adjacentPositions()
			.map { adjacentPosition -> soldierAtPosition(adjacentPosition) }
			.filterNotNull()

	private fun soldierAtPosition(position: BattlefieldPosition): SoldierInBattle? =
		soldiersInBattle.find { it.position.equals(position) }

	fun statusForSoldier(soldier: Soldier): SoldierStatus? = findSoldierInBattle(soldier)?.status

	private fun findSoldierInBattle(soldier: Soldier) =
		soldiersInBattle.find { it.soldier.equals(soldier) }

	fun soldiersFightingInOrderOfBattling(): List<SoldierInBattle> =
		soldiersInBattle.filter { it.action == SoldierAction.FIGHT }

	fun withSoldiersStatuses(newSoldiersStatuses: List<Pair<Soldier, SoldierStatus>>) : SoldiersInBattle =
		SoldiersInBattle(soldiersInBattle.map {
				val newStatusMaybe = newSoldiersStatuses.find { soldierAndStatus -> soldierAndStatus.first == it.soldier }?.second
				val newStatus = newStatusMaybe ?: it.status
				SoldierInBattle(it.soldier, it.position, newStatus, it.action)
			}
		)

	fun allSoldiers(): List<Soldier> = soldiersInBattle.map { it.soldier }
}