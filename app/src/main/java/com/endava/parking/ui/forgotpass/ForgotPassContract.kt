package com.endava.parking.ui.forgotpass

interface ForgotPassContract {

    interface View {

        fun setButtonAvailability(isEnable: Boolean)
    }

    interface Presenter {

        fun checkUserValidation(mail: String)

        fun submitData(email: String)
    }
}
