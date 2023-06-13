package com.endava.parking.ui.signup

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.User
import com.endava.parking.databinding.FragmentSignUpBinding
import com.endava.parking.ui.utils.makeTextClickable
import com.endava.parking.utils.EmailValidator
import com.endava.parking.utils.NameValidator
import com.endava.parking.utils.PasswordValidator
import com.endava.parking.utils.PhoneValidator

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate), SignUpContract.View {

    private val presenter = SignUpPresenter(
        this,
        NameValidator(),
        EmailValidator(),
        PasswordValidator(),
        PhoneValidator()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() = with(binding) {
        setOnKeyboardCloseListener {
            if (inputEmail.text.toString() != "") {
                presenter.validateField(InputTextType.PHONE, inputPhone.text.toString())
            }
        }
        inputName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                presenter.validateField(InputTextType.NAME, inputName.text.toString())
            }
        }
        inputEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                presenter.validateField(InputTextType.EMAIL, inputEmail.text.toString())
            }
        }
        inputPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                presenter.validateField(InputTextType.PASSWORD, inputPassword.text.toString())
            }
        }
        inputPhone.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                presenter.validateField(InputTextType.PHONE, inputPhone.text.toString())
            }
        }
        inputPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                inputPhone.requestFocus()
            }
            true
        }
        inputPhone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.setNextFocus(getUser())
            }
            false
        }
        inputName.addTextChangedListener { presenter.checkUserValidation(getUser()) }
        inputEmail.addTextChangedListener { presenter.checkUserValidation(getUser()) }
        inputPassword.addTextChangedListener { presenter.checkUserValidation(getUser()) }
        inputPhone.addTextChangedListener { presenter.checkUserValidation(getUser()) }
        tvAgreementMessage.makeTextClickable(
            phrase = resources.getString(R.string.generic_terms_conditions_phrase),
            phraseColor = R.color.dark_gray,
            font = resources.getFont(R.font.mulish_700),
            onClickListener = {
                // todo: implement navigation
                Toast.makeText(
                    context,
                    "Was pressed Terms and Conditions from SignUpFragment",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        setupToolbarNavigation()

        // test code lets us know that the Submit button works
        btnConfirm.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Was pressed Submit button in Sign Up Fragment",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun showErrorMessage(fieldType: InputTextType, errorMessageId: Int) {
        with(binding) {
            when (fieldType) {
                InputTextType.NAME -> {
                    inputNameLayout.isErrorEnabled = true
                    inputNameLayout.error = resources.getString(errorMessageId)
                }

                InputTextType.EMAIL -> {
                    inputEmailLayout.isErrorEnabled = true
                    inputEmailLayout.error = resources.getString(errorMessageId)
                }

                InputTextType.PASSWORD -> {
                    inputPasswordLayout.isErrorEnabled = true
                    inputPasswordLayout.errorIconDrawable = null
                    inputPasswordLayout.error = resources.getString(errorMessageId)
                }

                InputTextType.PHONE -> {
                    inputPhoneLayout.isErrorEnabled = true
                    inputPhoneLayout.error = resources.getString(errorMessageId)
                }
            }
        }
    }

    override fun clearErrorMessage(fieldType: InputTextType) {
        with(binding) {
            when (fieldType) {
                InputTextType.NAME -> {
                    inputNameLayout.isErrorEnabled = false
                    inputNameLayout.error = null
                }

                InputTextType.EMAIL -> {
                    inputEmailLayout.isErrorEnabled = false
                    inputEmailLayout.error = null
                }

                InputTextType.PASSWORD -> {
                    inputPasswordLayout.isErrorEnabled = false
                    inputPasswordLayout.error = null
                }

                InputTextType.PHONE -> {
                    inputPhoneLayout.isErrorEnabled = false
                    inputPhoneLayout.error = null
                }
            }
        }
        presenter.checkUserValidation(getUser())
    }

    override fun moveFocusTo(nextFocusedField: InputTextType?) {
        when (nextFocusedField) {
            InputTextType.NAME -> binding.inputName.requestFocus()
            InputTextType.EMAIL -> binding.inputEmail.requestFocus()
            InputTextType.PASSWORD -> binding.inputPassword.requestFocus()
            else -> binding.btnConfirm.requestFocus()
        }
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            navigationCallback.popBackStack()
        }
    }

    override fun setButtonAvailability(isEnable: Boolean) {
        binding.btnConfirm.isEnabled = isEnable
    }

    private fun getUser(): User = User(
        binding.inputName.text.toString(),
        binding.inputEmail.text.toString(),
        binding.inputPassword.text.toString(),
        binding.inputPhone.text.toString()
    )

    companion object {
        const val TAG = "SignUpFragment"
    }
}
