package apuchau.skirmish.app

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import apuchau.skirmish.app.assets.readImageAsset
import apuchau.skirmish.app.view.crop
import apuchau.skirmish.app.view.rgb8Content
import com.natpryce.onError
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SkirmishAppActivityTests {

	@get:Rule
	val activityRule = ActivityTestRule<SkirmishAppActivity>(SkirmishAppActivity::class.java)

	@Test
	fun when_started_activity_displays_battlefield_with_arthur_and_mordred_then_log() {

		val expectedContent = readTestImage("battlefield_1000x800_battlefield_arthur_mordred_then_log.png")
		val viewContent = mainView().rgb8Content().crop(0, 0, 1000, 800)

		assertBitmaps(expectedContent, viewContent)
	}

	private fun readTestImage(imagePath: String): Bitmap =
		context().readImageAsset(imagePath).onError { throw Exception("Error getting test image: $imagePath") }

	private fun assertBitmaps(expectedBitmap: Bitmap, bitmap: Bitmap) {

		assertEquals("width", expectedBitmap.width, bitmap.width)
		assertEquals("height", expectedBitmap.height, bitmap.height)

		(0 until expectedBitmap.width).forEach { x ->
			(0 until expectedBitmap.height).forEach { y ->

				val expectedColor = expectedBitmap.getPixel(x, y)
				val color = bitmap.getPixel(x, y)

				assertEquals("Pixel colour at ($x,$y)", toRGBString(expectedColor), toRGBString(color))
			}
		}
	}

	private fun toRGBString(pixelValue: Int): String {
		return Integer.toHexString(pixelValue)
	}

	private fun context(): Context = InstrumentationRegistry.getContext()

	private fun mainView() : View = activityRule.activity.findViewById<View>(R.id.content)
}