package apuchau.kotlin.result

import com.natpryce.Err
import com.natpryce.Result
import kotlin.test.assertEquals

fun assertError(result: Result<Any, String>, errMsg: String) {
	assertEquals(Err(errMsg), result)
}
