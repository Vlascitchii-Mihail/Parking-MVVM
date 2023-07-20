package com.endava.parking.ui.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.endava.parking.data.model.WorkingDay

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

fun TextView.colorWorkingDays(days: List<String>?, color: Int, mode24_7: Boolean) {

    val workingDays: ArrayList<WorkingDay> = arrayListOf()

    /** Set week, as - not working */
    if (!mode24_7) {
        for (i in 1..7) { workingDays.add(WorkingDay(i, false)) }
    }

    /** Each day from backend, set as working day */
    if (days?.isEmpty() == false) {
        for (day in DaysOfWeek.values()) {
            if (days.contains(day.printableName)) { workingDays[day.ordinal].isWorking = true }
        }
    }
    val spannable = SpannableString(text)
    var start: Int
    var end: Int
    var separatorsCount: Int
    workingDays.forEach {
        /** Count separators between day symbols - S/M/T/W/T/F/S,
         * for every single day, it help us to find start and end positions */
        separatorsCount = it.weekDayNumber - 1
        end = it.weekDayNumber + separatorsCount
        start = end - 1
        if (!it.isWorking) { spannable.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
    }
    text = spannable
}
