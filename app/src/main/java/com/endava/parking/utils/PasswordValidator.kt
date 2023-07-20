package com.endava.parking.utils

import java.util.regex.Pattern
import javax.inject.Inject

private const val PASSWORD_REGEX =
    "^(?=.*[A-Z])(?=.*?[0-9])(?=.*?[`~!@#$%^&*()_+=\"':;?\\\\/])[A-Za-z0-9`~!@#\$%^&*()_+=\"':;?\\\\/]{5,10}$"

class PasswordValidator @Inject constructor() : Validator {

    override fun validate(password: CharSequence): Boolean =
        Pattern.compile(PASSWORD_REGEX).matcher(password).matches()
}
