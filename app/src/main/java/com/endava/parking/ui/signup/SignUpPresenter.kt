package com.endava.parking.ui.signup

import com.endava.parking.R
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

    fun setNextFocus(user: User) {
        if (phoneValidator.validate(user.phone)) {
            if (!passwordValidator.validate(user.password)) view.moveFocusTo(InputTextType.PASSWORD)
            if (!emailValidator.validate(user.email)) view.moveFocusTo(InputTextType.EMAIL)
            if (!nameValidator.validate(user.name)) view.moveFocusTo(InputTextType.NAME)
        } else validateField(InputTextType.PHONE, user.phone)
    }

    override fun validateField(fieldType: InputTextType, text: CharSequence) {
        val isValid: Boolean
        val errorMessageId: Int
        when (fieldType) {
            InputTextType.NAME -> {
                isValid = nameValidator.validate(text)
                errorMessageId = R.string.error_message_name
            }
            InputTextType.EMAIL -> {
                isValid = emailValidator.validate(text)
                errorMessageId = R.string.error_message_email
            }
            InputTextType.PASSWORD -> {
                isValid = passwordValidator.validate(text)
                errorMessageId = R.string.error_message_password
            }
            InputTextType.PHONE -> {
                isValid = phoneValidator.validate(text)
                errorMessageId = R.string.error_message_phone
            }
        }
        if (isValid) {
            view.clearErrorMessage(fieldType)
        } else {
            view.showErrorMessage(fieldType, errorMessageId)
        }
    }

    override fun submitData(user: User) {}
}
