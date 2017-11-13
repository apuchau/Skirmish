package apuchau.skirmish.army

import apuchau.skirmish.soldier.Soldier
import com.natpryce.Err
import com.natpryce.Ok
import com.natpryce.Result
import com.natpryce.map

class Army private constructor(var armyId: String, var soldiers: Set<Soldier>) {

	companion object Factory {

		fun create(armyId: String, soldiers: Set<Soldier>) : Result<Army,String> {

			return checkArmyHasSoldiers(soldiers)
				.map { Army(armyId, soldiers) }
		}

		private fun checkArmyHasSoldiers(soldiers: Set<Soldier>) : Result<Unit, String> {
			return if (soldiers.isEmpty()) {
				Err("You need at least one soldier in an army")
			}
			else {
				Ok(Unit)
			}
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