package com.endava.parking.ui.signup

import com.endava.parking.databinding.FragmentSignUpBinding
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.InputTextType
import com.google.android.material.textfield.TextInputLayout

class SignUpErrorHandler(private val binding: FragmentSignUpBinding) {

    fun setErrorStates(states: List<InputState>) {
        with(binding) {
            states.forEach {
                when (it.fieldType) {
                    InputTextType.NAME -> {
                        if (it.isValid) hideError(inputNameLayout)
                        else  showError(inputNameLayout, it.errorMessage)
                    }
                    InputTextType.EMAIL -> {
                        if (it.isValid) hideError(inputEmailLayout)
                        else  showError(inputEmailLayout, it.errorMessage)
                    }
                    InputTextType.PHONE -> {
                        if (it.isValid) hideError(inputPhoneLayout)
                        else showError(inputPhoneLayout, it.errorMessage)
                    }
                    InputTextType.PASSWORD -> {
                        if (it.isValid) hideError(inputPasswordLayout)
                        else {
                            showError(inputPasswordLayout, it.errorMessage)
                            inputPasswordLayout.errorIconDrawable = null
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showError(inputLayout: TextInputLayout, errorMessage: Int) {
        inputLayout.isErrorEnabled = true
        inputLayout.error = binding.root.resources.getString(errorMessage)
    }

    private fun hideError(inputLayout: TextInputLayout) {
        inputLayout.isErrorEnabled = false
        inputLayout.error = null
    }
}
