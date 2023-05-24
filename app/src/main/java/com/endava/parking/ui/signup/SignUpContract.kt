package com.endava.parking.ui.signup

import com.endava.parking.data.User

interface SignUpContract {

    interface View {

        fun setButtonAvailability(isEnable: Boolean)
    }

    interface Presenter {

        fun checkUserValidation(user: User)

        fun submitData(user: User)
    }
}
