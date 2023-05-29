package com.endava.parking.ui.forgotpass

interface ForgotPassContract {

    interface View {

        fun setButtonAvailability(isEnable: Boolean)

        fun showErrorMessage(messageId: Int)

        fun clearErrorMessage()
    }

    interface Presenter {

        fun checkUserValidation(mail: String)

        fun submitData(email: String)
    }
}
