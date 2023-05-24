package com.endava.parking.ui.signup

import com.endava.parking.data.User
import com.endava.parking.utils.Validator

class SignUpPresenter(
    private val view: SignUpContract.View,
    private val nameValidator: Validator,
    private val emailValidator: Validator,
    private val passwordValidator: Validator,
    private val phoneValidator: Validator
) : SignUpContract.Presenter {

    override fun checkUserValidation(user: User) =
        view.setButtonAvailability(
            nameValidator.validate(user.name) &&
            emailValidator.validate(user.email) &&
            passwordValidator.validate(user.password) &&
            phoneValidator.validate(user.phone)
        )

    override fun submitData(user: User) {}
}
