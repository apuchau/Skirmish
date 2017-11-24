package apuchau.skirmish.battle.log

class BattleLog private constructor(private val logEntries : List<String>) {

	companion object Factory {
		fun empty() : BattleLog = BattleLog(emptyList())
	}

	fun byAddingEntries(logEntries: List<String>): BattleLog {

		val mergedEntries : MutableList<String> = this.logEntries.toMutableList()
		mergedEntries.addAll(logEntries)
		return BattleLog(mergedEntries.toList())
	}

	override fun hashCode(): Int = 0

	override fun equals(other: Any?): Boolean =
		other != null
			&& other is BattleLog

	override fun toString(): String = "BattleLog"

	fun contains(logEntry: String): Boolean = logEntries.contains(logEntry)
}