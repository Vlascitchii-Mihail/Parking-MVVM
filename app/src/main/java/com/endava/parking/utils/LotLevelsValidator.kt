package com.endava.parking.utils

import java.util.regex.Pattern
import javax.inject.Inject

private const val LOTS_AMOUNT_REGEX = "^(?!(0))[0-9]{1,3}\$"

class LotLevelsValidator @Inject constructor() : Validator {
    override fun validate(levelsQuantity: CharSequence): Boolean {
        val pattern = Pattern.compile(LOTS_AMOUNT_REGEX)
        val matcher = pattern.matcher(levelsQuantity)
        return matcher.matches() && (levelsQuantity.toString().toInt() <= 150)
    }
}
