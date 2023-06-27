package com.endava.parking.utils

import android.text.InputFilter
import android.text.Spanned

class BlankSpacesInputFilter : InputFilter {
    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int
    ): CharSequence {
        val regex = Regex("\\s")
        return source?.replace(regex, "").toString()
    }
}
