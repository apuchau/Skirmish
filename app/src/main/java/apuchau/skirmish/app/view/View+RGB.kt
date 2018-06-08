package apuchau.skirmish.app.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun View.rgb8Content() : Bitmap {
	val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
	val c = Canvas(bitmap)
	layout(getLeft(), getTop(), getRight(), getBottom())
	draw(c)
	return bitmap
}