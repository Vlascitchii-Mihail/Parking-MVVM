package com.endava.parking.ui.utils

import android.content.Context
import android.text.format.DateFormat
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getTimePicker(context: Context, pickerTitle: String): MaterialTimePicker {
    val isSystem24Hour = DateFormat.is24HourFormat(context)
    val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

    return MaterialTimePicker.Builder()
        .setTimeFormat(clockFormat)
        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
        .setTitleText(pickerTitle)
        .build()
}

fun getFormattedTime(hour: Int, minute: Int) =
    LocalDate.now().atTime(hour, minute)
        .format(DateTimeFormatter.ofPattern("HH:mm"))
