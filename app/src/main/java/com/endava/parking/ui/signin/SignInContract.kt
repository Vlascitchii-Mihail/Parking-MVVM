package com.endava.parking.ui.signin

import com.endava.parking.data.User
import com.endava.parking.ui.signup.InputTextType

interface SignInContract {

    interface View {

        fun setButtonAvailability(isEnable: Boolean)

        fun setErrorMessage(inputType: InputTextType, hasError: Boolean)
    }

    interface Presenter {

        fun checkButtonState(email: String, password: String)

        fun submitData(user: User)

        fun validateInput(inputType: InputTextType, input: String)
    }
}
