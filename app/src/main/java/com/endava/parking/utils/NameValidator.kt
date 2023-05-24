package com.endava.parking.utils

import java.util.regex.Pattern

private const val NAME_REGEX = "^[A-Za-z]+$"
private const val MIN_NAME_LENGTH = 3
private const val MAX_NAME_LENGTH = 20

class NameValidator : Validator {

    override fun validate(name: CharSequence): Boolean {
        val pattern = Pattern.compile(NAME_REGEX)
        val matcher = pattern.matcher(name)
        return matcher.matches()
                && name.length >= MIN_NAME_LENGTH
                && name.length <= MAX_NAME_LENGTH
    }
}
