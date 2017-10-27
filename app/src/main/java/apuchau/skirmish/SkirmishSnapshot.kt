package apuchau.skirmish

data class SkirmishSnapshot(val width: Int, val height: Int) {

    override fun toString(): String {
        return "SkirmishSnapshot. width: $width, height $height";
    }
}