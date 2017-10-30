package apuchau.skirmish

class Battlefield(val boundaries : BattlefieldBoundaries) {

    override fun hashCode(): Int {
        return boundaries.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other != null)
                    && (other is Battlefield)
                    && (boundaries == other.boundaries)
    }
}