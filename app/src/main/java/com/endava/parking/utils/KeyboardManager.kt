package com.endava.parking.utils

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import kotlin.math.roundToInt

private const val ROUNDING_CONST = 50F

class KeyboardManager(private val root: View) {

    private var globalLayoutListener : ViewTreeObserver.OnGlobalLayoutListener? = null

    fun setOnCloseActionListener(closeListener: () -> Unit) {
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            if (!isKeyboardOpen()) {
                closeListener.invoke()
            }
        }
        root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private fun isKeyboardOpen(): Boolean {
        val visibleBounds = Rect()
        root.getWindowVisibleDisplayFrame(visibleBounds)
        val heightDiff = root.height - visibleBounds.height()
        val marginOfError = this.convertDpToPx(ROUNDING_CONST).roundToInt()
        return heightDiff > marginOfError
    }

    private fun convertDpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            root.resources.displayMetrics
        )
    }

    fun unregister() {
        root.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        globalLayoutListener = null
    }
}
