package apuchau.skirmish.battle

import apuchau.skirmish.soldier.Soldier
import apuchau.skirmish.soldier.SoldierStatus

class SoldiersStatuses private constructor(val soldiersStatuses: Map<Soldier,SoldierStatus>) {

	companion object Factory {
		fun withAllHealthy(soldiers: Collection<Soldier>) : SoldiersStatuses {
			return SoldiersStatuses(
				soldiers.map { soldier -> Pair(soldier, SoldierStatus.HEALTHY) }.toMap()
			)
		}
	}

	override fun toString(): String = "${soldiersStatuses}"

	override fun hashCode(): Int = soldiersStatuses.hashCode()

	override fun equals(other: Any?): Boolean =
		(other != null)
			&& (other is SoldiersStatuses)
			&& (soldiersStatuses == other.soldiersStatuses)

	fun soldierStatus(soldier: Soldier): SoldierStatus? =
		soldiersStatuses.get(soldier)

	fun areAllSoldiersHealthy(): Boolean =
		soldiersStatuses.values.all { it == SoldierStatus.HEALTHY }

	fun areAllSoldiersDead(): Boolean =
		soldiersStatuses.values.all { it == SoldierStatus.DEAD }

	fun byChangingSoldierStatus(soldier: Soldier, status: SoldierStatus): SoldiersStatuses {

		val newSoldiersStatuses = soldiersStatuses.toMutableMap()
		newSoldiersStatuses.put(soldier, status)
		return SoldiersStatuses(newSoldiersStatuses)
	}

	fun soldiersAlive(): List<Soldier> {
		return soldiersStatuses
			.entries.filterNot { mapEntry -> mapEntry.value == SoldierStatus.DEAD }
			.map { mapEntry -> mapEntry.key }
	}

}