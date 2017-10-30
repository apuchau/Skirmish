package apuchau.skirmish

class Battlefield(val width: Int, val height: Int) {

    val boundaries: BattlefieldBoundaries

    init {
        boundaries = BattlefieldBoundaries(width, height)
    }

    override fun hashCode(): Int {
        return width + height
    }

    override fun equals(other: Any?): Boolean {
        return (other != null)
                    && (other is Battlefield)
                    && (width == other.width)
                    && (height == other.height)
    }
}