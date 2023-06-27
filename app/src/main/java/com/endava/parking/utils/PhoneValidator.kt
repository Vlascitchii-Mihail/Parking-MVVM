package com.endava.parking.utils

import java.util.regex.Pattern
import javax.inject.Inject

private const val PHONE_NUMBER_REGEX = "^[+]?[0-9]{8}+$"

class PhoneValidator @Inject constructor() : Validator {

    override fun validate(phoneNumber: CharSequence): Boolean {
        val pattern = Pattern.compile(PHONE_NUMBER_REGEX)
        val matcher = pattern.matcher(phoneNumber)
        return matcher.matches()
    }
}
