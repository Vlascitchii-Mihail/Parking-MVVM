package com.endava.parking.ui.forgotpass

import com.endava.parking.utils.Validator

class ForgotPassPresenter(
    private val view: ForgotPassContract.View,
    private val emailValidator: Validator
) : ForgotPassContract.Presenter {

    override fun checkUserValidation(mail: String) {
        view.setButtonAvailability(emailValidator.validate(mail))
    }

    override fun submitData(email: String) {}
}
