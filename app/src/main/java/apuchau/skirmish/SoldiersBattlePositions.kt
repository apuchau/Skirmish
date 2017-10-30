package apuchau.skirmish

import apuchau.skirmish.exception.DuplicatedSoldier
import apuchau.skirmish.exception.InvalidSoldiersPosition

class SoldiersBattlePositions(positions : Collection<Pair<Soldier,BattlefieldPosition>>) {

	var positions: List<Pair<Soldier,BattlefieldPosition>>

	init {
		checkNoDuplicateSoldiers(positions)
		checkSoldiersDontOccupySameSpaces(positions)

		this.positions = positions.toList()
	}

	private fun checkNoDuplicateSoldiers(positions: Collection<Pair<Soldier, BattlefieldPosition>>) {

		val numDifferentSoldiers =
			positions
				.map{it.first}
				.distinct()
				.count()

		if (positions.size != numDifferentSoldiers) {
			throw DuplicatedSoldier("There are duplicated positions in the battle")
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

	fun count(): Int {
		return positions.count()
	}

	override fun toString(): String {
		return positions.toString()
	}

	override fun hashCode(): Int {
		return positions.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		return (other != null)
			&& (other is SoldiersBattlePositions)
			&& (positions == other.positions)
	}

}