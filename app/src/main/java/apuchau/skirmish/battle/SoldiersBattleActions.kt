package apuchau.skirmish.battle

import apuchau.skirmish.soldier.Soldier

class SoldiersBattleActions private constructor(val actionsBySoldier : Map<Soldier, SoldierAction>) {

	companion object Factory {
		fun withAllDoingNothing(soldiers : Collection<Soldier>) : SoldiersBattleActions =
			SoldiersBattleActions(
				soldiers.map { soldier -> Pair(soldier, SoldierAction.DO_NOTHING) }
			)
	}

	constructor(soldiersActions: Collection<Pair<Soldier, SoldierAction>>) : this(soldiersActions.toMap())

	override fun toString(): String = "Soldiers actions. ${this.actionsBySoldier}"

	override fun hashCode(): Int = actionsBySoldier.hashCode()

	override fun equals(other: Any?): Boolean =
		other != null
			&& other is SoldiersBattleActions
			&& actionsBySoldier.equals(other.actionsBySoldier)

	fun byChangingSoldiersActions(soldiersActions: Collection<Pair<Soldier,SoldierAction>>): SoldiersBattleActions {

		val newSoldiersActions = actionsBySoldier.toMutableMap()
		soldiersActions.forEach { pair -> newSoldiersActions.put(pair.first, pair.second) }

		return SoldiersBattleActions(newSoldiersActions)
	}

	fun areAllSoldiersDoing(expectedAction: SoldierAction): Boolean =
		actionsBySoldier.values.all { action -> action == expectedAction }

	fun soldiersThatAreFighting(): Set<Soldier> =
		actionsBySoldier.entries
			.filter { entry -> entry.value == SoldierAction.FIGHT }
			.map { entry -> entry.key }
			.toSet()

	fun actionForSoldier(soldier: Soldier): SoldierAction? = actionsBySoldier.get(soldier)

}