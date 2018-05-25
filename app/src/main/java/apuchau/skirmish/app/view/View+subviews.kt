package apuchau.skirmish.app.view

import android.view.View
import android.view.ViewGroup

fun View.subviews() : List<View> {

	return if (this is ViewGroup) {
		(0 until this.childCount)
			.map { it -> this.getChildAt(it) }
			.toList()
	}
	else {
		emptyList()
	}
}