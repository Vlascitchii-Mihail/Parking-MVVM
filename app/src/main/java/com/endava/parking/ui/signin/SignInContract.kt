package com.endava.parking.ui.signin

import com.endava.parking.data.User

interface SignInContract {

    interface View {

        fun setButtonAvailability(isEnable: Boolean)
    }

    interface Presenter {

        fun checkUserValidation(email: String, password: String)

        fun submitData(user: User)
    }
}
