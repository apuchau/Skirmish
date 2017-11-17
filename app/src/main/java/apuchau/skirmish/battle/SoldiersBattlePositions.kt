package apuchau.skirmish.battle

import apuchau.skirmish.battlefield.BattlefieldBoundaries
import apuchau.skirmish.battlefield.BattlefieldPosition
import apuchau.skirmish.soldier.Soldier
import com.natpryce.*

class SoldiersBattlePositions internal constructor(positions : Collection<Pair<Soldier,BattlefieldPosition>>) {

	private var soldiersAndPositions: List<Pair<Soldier, BattlefieldPosition>>

	companion object Factory {

		fun create(
			positions : Collection<Pair<Soldier,BattlefieldPosition>>)
			: Result<SoldiersBattlePositions, String> {

			return checkNoDuplicateSoldiers(positions)
				.flatMap { checkSoldiersDontOccupySameSpaces(positions) }
				.map { SoldiersBattlePositions(positions) }
		}

		private fun checkNoDuplicateSoldiers(
			positions: Collection<Pair<Soldier, BattlefieldPosition>>)
			: Result<Unit, String> {

			val numDifferentSoldiers =
				positions
					.map{it.first}
					.distinct()
					.count()

			return check(
				positions.size == numDifferentSoldiers,
				"There are duplicated soldiers in the battle")
		}

		private fun checkSoldiersDontOccupySameSpaces(
			positions: Collection<Pair<Soldier, BattlefieldPosition>>)
			: Result<Unit,String> {

			val numDifferentPositions =
				positions
					.map{it.second}
					.distinct()
					.count()

			return check(
				positions.size == numDifferentPositions,
				"Some soldiers occupy the same battlefield spot")
		}

		private fun check(boolean: Boolean, errorMsg: String) : Result<Unit,String> {
			return if (boolean) Ok(Unit) else Err(errorMsg)
		}
	}

	init {
		this.soldiersAndPositions = positions.toList()
	}

	override fun toString(): String = soldiersAndPositions.toString()

	override fun hashCode(): Int = soldiersAndPositions.hashCode()

	override fun equals(other: Any?): Boolean =
		(other != null)
			&& (other is SoldiersBattlePositions)
			&& (soldiersAndPositions == other.soldiersAndPositions)

	fun soldiers() : List<Soldier> = soldiersAndPositions.map{ it.first }

	fun areAllWithinBounds(battlefieldBoundaries: BattlefieldBoundaries) =
		soldiersAndPositions
			.map{it.second}
			.all{it.isWithinBounds(battlefieldBoundaries)}

	fun containsSoldier(soldier : Soldier): Boolean =
		soldiersAndPositions
			.map { it.first }
			.any { it == soldier }

	fun soldiersAdjacentToSoldier(soldier: Soldier): Set<Soldier> {

		val position = soldierPosition(soldier)

			return if (position == null)
				emptySet()
					else
				position.adjacentPositions()
					.map { adjancentPosition -> soldierAtPosition(adjancentPosition) }
					.filterNotNull()
					.toSet()
	}

	private fun soldierAtPosition(position: BattlefieldPosition): Soldier? {

		val soldierAndPosition = soldiersAndPositions
			.find { it.second == position }

		return soldierAndPosition?.first
	}

	private fun soldierPosition(soldier: Soldier): BattlefieldPosition? {

		val soldierAndPosition = soldiersAndPositions
			.find { it.first == soldier }

		return soldierAndPosition?.second

	}

	fun forEach(fn: (Pair<Soldier,BattlefieldPosition>) -> Unit) {
		soldiersAndPositions.forEach(fn)
	}

}