package com.endava.parking.utils

import java.util.regex.Pattern
import javax.inject.Inject

private const val EMAIL_VALIDATOR = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,194}" +
    "\\@" +
    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,43}" +
    "(" +
    "\\." +
    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,15}" +
    ")+"

class EmailValidator @Inject constructor() : Validator {

    override fun validate(email: CharSequence): Boolean {
        return Pattern.compile(EMAIL_VALIDATOR).matcher(email).matches()
    }
}
