package apuchau.skirmish.app.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.natpryce.Err
import com.natpryce.Ok
import com.natpryce.Result
import com.natpryce.flatMap
import java.io.InputStream

fun Context.readImageAsset(filepath: String) : Result<Bitmap, String> =
	openAssetsFile(filepath)
		.flatMap { inStream : InputStream -> inStream.use { inStream.readAsBitmap() } }

fun InputStream.readAsBitmap() : Result<Bitmap,String> {

	try {
		val bitmap= BitmapFactory.decodeStream(this)
		if (bitmap == null) {
			return Err("InputStream doesn't contain a Bitmap")
		}
		else {
			return Ok(bitmap)
		}
	} catch(e: Exception) {
		return Err("Error reading InputStream as bitmap. $e")
	}
}


private fun Context.openAssetsFile(filepath: String): Result<InputStream, String> {
	try {
		val inStream= resources?.assets?.open("apuchau/skirmish/app/${filepath}")
		return if (inStream == null) {
			Err("Asset file '$filepath' not found")
		}
		else {
			Ok(inStream)
		}
	} catch (e: Exception) {
		return Err("Error opening asset file '$filepath'. $e")
	}
}