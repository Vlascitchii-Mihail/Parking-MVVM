package com.endava.parking.utils

import java.util.regex.Pattern

private const val PASSWORD_REGEX = "^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d).*\$"

class PasswordValidator : Validator {

    override fun validate(password: CharSequence): Boolean {
        val regex = PASSWORD_REGEX
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}
