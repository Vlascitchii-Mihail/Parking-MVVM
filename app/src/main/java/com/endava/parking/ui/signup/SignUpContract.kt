package com.endava.parking.ui.signup

import com.endava.parking.data.model.User
import com.endava.parking.ui.utils.InputTextType

interface SignUpContract {

    interface View {

        fun setButtonAvailability(isEnable: Boolean)

        fun showErrorMessage(fieldType: InputTextType, errorMessageId: Int)

        fun clearErrorMessage(fieldType: InputTextType)

        fun moveFocusTo(nextFocusedField: InputTextType?)
    }

    interface Presenter {

        fun checkUserValidation(user: User)

        fun validateField(fieldType: InputTextType, text: CharSequence)

        fun submitData(user: User)
    }
}
