package com.endava.parking.ui.signin

import com.endava.parking.data.User
import com.endava.parking.utils.Validator

class SignInPresenter(
    private val view: SignInContract.View,
    private val emailValidator: Validator,
    private val passwordValidator: Validator,
) : SignInContract.Presenter {

    override fun checkUserValidation(email: String, password: String) {
        view.setButtonAvailability(
            emailValidator.validate(email) && passwordValidator.validate(password)
        )
    }

    override fun submitData(user: User) {}
}
