package apuchau.skirmish.soldier

enum class SoldierStatus {
	HEALTHY, WOUNDED, DEAD;

	fun isAlive(): Boolean = !isDead()

	fun isDead(): Boolean = (this == DEAD)
}