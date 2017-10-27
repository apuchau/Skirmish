package apuchau.skirmish

import org.junit.Test
import org.junit.Assert.*

class SkirmishAcceptanceTests {

    @Test

    fun a_skirmish() {

        val skirmish = Skirmish(2,1);

        assertEquals(SkirmishSnapshot(2, 1), skirmish.getSnapshot());
    }
}
