package com.endava.parking.utils

import android.util.Patterns

class EmailValidator : Validator {

    override fun validate(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
