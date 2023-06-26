package com.endava.parking.utils

import android.util.Patterns
import javax.inject.Inject

class EmailValidator @Inject constructor() : Validator {

    override fun validate(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
