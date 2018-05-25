package apuchau.skirmish.app.view

import android.graphics.Bitmap
import java.io.OutputStream

fun Bitmap.crop(x: Int, y: Int, width: Int, height: Int) =
	Bitmap.createBitmap(this, x, y, width, height)

fun Bitmap.writeInPngFormatTo(outStream: OutputStream) {
	compress(Bitmap.CompressFormat.PNG, 100, outStream)
	// PNG is a lossless format, the compression factor (100) is ignored
}
