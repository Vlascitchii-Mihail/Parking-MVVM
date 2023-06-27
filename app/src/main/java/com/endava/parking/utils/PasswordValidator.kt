package com.endava.parking.utils

import java.util.regex.Pattern

private const val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*?[0-9])(?=.*?[!@#$%^&*()_+*\"':;?])[A-Za-z0-9!@#\$%^&*()_+*\"':;?]{5,10}$"

class PasswordValidator : Validator {

    override fun validate(password: CharSequence): Boolean {
        val regex = PASSWORD_REGEX
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}
