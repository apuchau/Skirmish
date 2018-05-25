package apuchau.skirmish.app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import apuchau.skirmish.app.view.crop
import apuchau.skirmish.app.view.rgb8Content
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SkirmishAppActivityTests {

	@get:Rule
	val activityRule = ActivityTestRule<SkirmishAppActivity>(SkirmishAppActivity::class.java)

	@Test
	fun when_started_activity_displays_battlefield_with_arthur_and_mordred_then_log() {

		val view = activityRule.activity.findViewById<View>(android.R.id.content)

		val expectedContent = readImage("battlefield_500x700_battlefield_arthur_mordred_then_log.png")
		val viewContent = view.rgb8Content().crop(0, 0, 500, 700)
		assertBitmaps(expectedContent, viewContent)
	}

	private fun readImage(imagePath: String): Bitmap {

		val ctx= InstrumentationRegistry.getContext()
		val inStream=
			ctx.resources?.assets?.open("apuchau/skirmish/app/${imagePath}")

		if (inStream == null) {
			throw IllegalStateException("Required image ${imagePath} not found")
		}

		try {
			return BitmapFactory.decodeStream(inStream)
				?: throw IllegalStateException("Error reading the image '${imagePath}'")
		} finally {
			inStream.close()
		}
	}

	private fun assertBitmaps(expectedBitmap: Bitmap, bitmap: Bitmap) {

		Assert.assertEquals("width", expectedBitmap.width, bitmap.width)
		Assert.assertEquals("height", expectedBitmap.height, bitmap.height)

		for (x in 0 until expectedBitmap.width) {
			for (y in 0 until expectedBitmap.height) {
				val expectedColor = expectedBitmap.getPixel(x, y)
				val color = bitmap.getPixel(x, y)

				Assert.assertEquals("Pixel colour at ($x,$y)", toRGB(expectedColor), toRGB(color))
			}
		}
	}

	private fun toRGB(pixelValue: Int): String {
		return Integer.toHexString(pixelValue)
	}

	private fun context(): Context? = InstrumentationRegistry.getTargetContext()
}