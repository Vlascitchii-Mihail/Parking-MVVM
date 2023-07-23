package com.endava.parking.utils

import java.util.regex.Pattern
import javax.inject.Inject

private const val PARKING_LOT_REGEX = "^[A-Za-z0-9/.,\"'\\-# №&!();:`@\\\\+]{2,70}$"

class LotDetailsValidator @Inject constructor(): Validator {
    override fun validate(lotDescription: CharSequence): Boolean {
        val pattern = Pattern.compile(PARKING_LOT_REGEX)
        val matcher = pattern.matcher(lotDescription)
        return matcher.matches()
    }
}
