package apuchau.skirmish.app.view

import android.content.Context
import android.view.View
import java.io.IOException
import java.io.OutputStream

fun View.takeSnapshot(applicationContext: Context, filepath: String) {
	var out : OutputStream? = null
	try {
		out = applicationContext.openFileOutput(filepath, Context.MODE_PRIVATE)
		rgb8Content().writeInPngFormatTo(out)
	}
	catch(e: IOException) {
		throw IOException("Error taking screenshot from View to '$filepath'", e)
	}
	finally {
		try { out?.close() } catch (e: IOException) {}
	}
}

