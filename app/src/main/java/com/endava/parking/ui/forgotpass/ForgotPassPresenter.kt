package com.endava.parking.ui.forgotpass

import com.endava.parking.R
import com.endava.parking.utils.Validator

class ForgotPassPresenter(
    private val view: ForgotPassContract.View,
    private val emailValidator: Validator
) : ForgotPassContract.Presenter {

    override fun checkUserValidation(mail: String) {
        if (emailValidator.validate(mail)) {
            view.clearErrorMessage()
            view.setButtonAvailability(true)
        } else {
            view.showErrorMessage(R.string.error_message_email)
            view.setButtonAvailability(false)
        }
    }

    override fun submitData(email: String) {}
}
