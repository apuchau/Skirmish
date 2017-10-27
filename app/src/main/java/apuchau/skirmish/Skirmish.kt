package apuchau.skirmish

class Skirmish(val width: Int, val height: Int) {

    fun getSnapshot(): SkirmishSnapshot {
        return SkirmishSnapshot(width, height);
    }

}
