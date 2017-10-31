package apuchau.skirmish

import apuchau.skirmish.exception.NotEnoughSoldiers

class Army(armyId: String, soldiers: Set<Soldier>) {

	private var armyId: String
	var soldiers: Set<Soldier>

	init {
		checkArmyHasSoldiers(soldiers)
		this.armyId = armyId
		this.soldiers = soldiers
	}

	private fun checkArmyHasSoldiers(soldiers: Set<Soldier>) {
		if (soldiers.isEmpty()) {
			throw NotEnoughSoldiers("You need at least one soldier in an army")
		}
	}

	fun containsSoldier(soldier: Soldier): Boolean = soldiers.contains(soldier)

	override fun hashCode(): Int = armyId.hashCode()

	override fun equals(other: Any?): Boolean =
			other != null
		&& other is Army
		&& armyId.equals(other.armyId)
	override fun toString(): String = "Army '$armyId': Soldiers: $soldiers"
}