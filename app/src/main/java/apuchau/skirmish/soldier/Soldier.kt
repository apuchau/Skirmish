package apuchau.skirmish.soldier

class Soldier(val soldierId: SoldierId) {

    override fun hashCode(): Int {
        return soldierId.hashCode();
    }

    override fun equals(other: Any?): Boolean {
        return (other != null)
           && (other is Soldier)
           && (soldierId == other.soldierId);
    }
}