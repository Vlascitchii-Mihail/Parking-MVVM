package com.endava.parking.utils

import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver

class KeyboardManager(private val root: View) {

    private var globalLayoutListener : ViewTreeObserver.OnGlobalLayoutListener? = null

    fun setOnStateChangeActionListener(
        openListener: (() -> Unit)? = null,
        closeListener: (() -> Unit)? = null
    ) {
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            if (isKeyboardOpen())
                openListener?.invoke()
            else
                closeListener?.invoke()
        }

        root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private fun isKeyboardOpen(): Boolean {
        val heightDiff = root.rootView.height - root.height
        return heightDiff > convertDpToPx(200F)
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
