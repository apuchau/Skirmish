package apuchau.skirmish.battle

import apuchau.skirmish.army.Army
import apuchau.skirmish.soldier.Soldier

class SoldiersBattleActions(val soldiersActionsByArmy: Map<Army, Map<Soldier, SoldierAction>>) {

	companion object Factory {
		fun withAllDoingNothing(armies : Collection<Army>) : SoldiersBattleActions =
			SoldiersBattleActions(
				armies
					.map { army -> Pair(army, army.soldiers.map { soldier -> Pair(soldier, SoldierAction.DO_NOTHING) }.toMap() )}
					.toMap()
			)
	}

	fun byChangingSoldiersActions(soldiersActions: Collection<Pair<Soldier,SoldierAction>>): SoldiersBattleActions {

		val newSoldiersActionsByArmy = soldiersActionsByArmy.entries.map {
			entry -> Pair(entry.key, calculateMergedActionsMap(entry.value, soldiersActions)) }.toMap()

		return SoldiersBattleActions(newSoldiersActionsByArmy)
	}

	private fun calculateMergedActionsMap(

		originalSoldierActions: Map<Soldier, SoldierAction>,
		newSoldiersActions: Collection<Pair<Soldier, SoldierAction>>): Map<Soldier, SoldierAction> {

		val mergedSoldierActions = originalSoldierActions.toMutableMap()
		newSoldiersActions
			.filter { pair -> originalSoldierActions.containsKey(pair.first) }
			.forEach { pair -> mergedSoldierActions.put(pair.first, pair.second) }

		return mergedSoldierActions
	}

	fun areAllSoldiersDoing(expectedAction: SoldierAction): Boolean =
		soldiersActionsByArmy
			.map { armyToSoldiersActions -> armyToSoldiersActions.value }
			.flatMap { soldiersToActions -> soldiersToActions.values }
			.all { action -> action == expectedAction }

	override fun toString(): String = "Soldiers actions. ${this.soldiersActionsByArmy}"

	override fun hashCode(): Int = soldiersActionsByArmy.hashCode()

	override fun equals(other: Any?): Boolean =
		other != null
			&& other is SoldiersBattleActions
			&& soldiersActionsByArmy.equals(other.soldiersActionsByArmy)

}