package com.endava.parking.utils

import java.util.regex.Pattern
import javax.inject.Inject

private const val NAME_REGEX = "^[A-Za-z]+$"
private const val MIN_NAME_LENGTH = 1
private const val MAX_NAME_LENGTH = 30

class NameValidator @Inject constructor() : Validator {

    override fun validate(name: CharSequence): Boolean {
        val pattern = Pattern.compile(NAME_REGEX)
        val matcher = pattern.matcher(name)
        return matcher.matches() &&
            name.length >= MIN_NAME_LENGTH &&
            name.length <= MAX_NAME_LENGTH
    }
}
