package com.endava.parking.ui.utils

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun TextView.makeTextClickable(
    phrase: String,
    phraseColor: Int,
    font: Typeface,
    onClickListener: () -> Unit
) {

    val start = text.indexOf(phrase)
    if (start == -1) return
    val end = start + phrase.length
    val spannableString = SpannableString(text)

    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = phraseColor
            ds.isUnderlineText = true
            ds.typeface = font
        }

        override fun onClick(widget: View) {
            onClickListener.invoke()
        }
    }

    spannableString.setSpan(
        clickableSpan,
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    this.movementMethod = LinkMovementMethod.getInstance()
    this.text = spannableString
}
