package com.endava.parking.ui.signin

import com.endava.parking.data.User
import com.endava.parking.ui.signup.InputTextType
import com.endava.parking.utils.Validator

class SignInPresenter(
    private val view: SignInContract.View,
    private val emailValidator: Validator,
    private val passwordValidator: Validator,
) : SignInContract.Presenter {

    override fun checkButtonState(email: String, password: String) {
        view.setButtonAvailability(
            emailValidator.validate(email) && passwordValidator.validate(password)
        )
    }

    override fun submitData(user: User) {}

    override fun validateInput(inputType: InputTextType, input: String) {
        when (inputType) {
            InputTextType.EMAIL -> {
                view.setErrorMessage(inputType, !emailValidator.validate(input))
            }
            else -> {
                view.setErrorMessage(inputType, !passwordValidator.validate(input))
            }
        }
    }
}
