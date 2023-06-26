package com.endava.parking.utils

import java.util.regex.Pattern
import javax.inject.Inject

private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*])(?=\\S+$).{5,10}$"

class PasswordValidator @Inject constructor() : Validator {

    override fun validate(password: CharSequence): Boolean {
        val regex = PASSWORD_REGEX
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}
