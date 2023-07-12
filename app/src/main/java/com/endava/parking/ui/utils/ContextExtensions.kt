package com.endava.parking.ui.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.showToast(resId: Int) {
    showToast(this.resources.getString(resId))
}

fun Context.showLongToast(message: String) {
    showToast(message, Toast.LENGTH_LONG)
}

fun Context.showLongToast(resId: Int) {
    showToast(this.resources.getString(resId), Toast.LENGTH_LONG)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
