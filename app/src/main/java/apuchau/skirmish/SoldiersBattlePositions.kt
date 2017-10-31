package apuchau.skirmish

import apuchau.skirmish.exception.DuplicatedSoldier
import apuchau.skirmish.exception.InvalidSoldiersPosition

class SoldiersBattlePositions(positions : Collection<Pair<Soldier,BattlefieldPosition>>) {

	private var soldiersAndPositions: List<Pair<Soldier,BattlefieldPosition>>

	init {
		checkNoDuplicateSoldiers(positions)
		checkSoldiersDontOccupySameSpaces(positions)

		this.soldiersAndPositions = positions.toList()
	}

	private fun checkNoDuplicateSoldiers(positions: Collection<Pair<Soldier, BattlefieldPosition>>) {

		val numDifferentSoldiers =
			positions
				.map{it.first}
				.distinct()
				.count()

		if (positions.size != numDifferentSoldiers) {
			throw DuplicatedSoldier("There are duplicated soldiersAndPositions in the battle")
		}
	}

	private fun checkSoldiersDontOccupySameSpaces(positions: Collection<Pair<Soldier, BattlefieldPosition>>) {

		val numDifferentPositions =
			positions
				.map{it.second}
				.distinct()
				.count()

		if (positions.size != numDifferentPositions) {
			throw InvalidSoldiersPosition("Some soldiers occupy the same battlefield spot")
		}
	}

	fun soldiers() : List<Soldier> = soldiersAndPositions.map{ it.first }

	fun areAllWithinBounds(battlefieldBoundaries: BattlefieldBoundaries) =
		soldiersAndPositions
			.map{it.second}
			.all{it.isWithinBounds(battlefieldBoundaries)}

	fun containsSoldier(soldier : Soldier): Boolean =
		soldiersAndPositions
			.map { it.first }
			.any { it == soldier }

	override fun toString(): String = soldiersAndPositions.toString()

	override fun hashCode(): Int = soldiersAndPositions.hashCode()

	override fun equals(other: Any?): Boolean =
		(other != null)
			&& (other is SoldiersBattlePositions)
			&& (soldiersAndPositions == other.soldiersAndPositions)

}