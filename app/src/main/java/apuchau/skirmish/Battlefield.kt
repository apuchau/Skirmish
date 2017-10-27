package apuchau.skirmish

class Battlefield(val width: Int, val height: Int) {

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