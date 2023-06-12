package com.endava.parking.ui.signin

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.databinding.FragmentSignInBinding
import com.endava.parking.ui.forgotpass.ForgotPassFragment
import com.endava.parking.ui.signup.InputTextType
import com.endava.parking.ui.utils.makeTextClickable
import com.endava.parking.utils.EmailValidator
import com.endava.parking.utils.PasswordValidator

class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate), SignInContract.View {

    private val presenter = SignInPresenter(
        this,
        EmailValidator(),
        PasswordValidator()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupNavigation()
    }

    private fun setupView() = with(binding) {
        tvTermsConditions.makeTextClickable(
            phrase = resources.getString(R.string.generic_terms_conditions_phrase),
            phraseColor = R.color.dark_gray,
            font = resources.getFont(R.font.mulish_700),
            onClickListener = {
                // todo: implement navigation
                Toast.makeText(
                    context,
                    "Was pressed Terms and Conditions from SignInFragment",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        inputEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                presenter.validateInput(InputTextType.EMAIL, inputEmail.text.toString())
            }
        }

        inputPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                presenter.validateInput(InputTextType.PASSWORD, inputPassword.text.toString())
            }
        }

        inputPassword.setOnEditorActionListener { _, actionId, _ ->
            //clear the focus if was clicked the keyboard done button
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.validateInput(InputTextType.PASSWORD, inputPassword.text.toString())
            }
            return@setOnEditorActionListener true
        }

        inputEmail.addTextChangedListener {
            presenter.checkButtonState(
                inputEmail.text.toString(),
                inputPassword.text.toString()
            )
        }

        inputPassword.addTextChangedListener {
            presenter.checkButtonState(
                inputEmail.text.toString(),
                inputPassword.text.toString()
            )
        }

        //test code lets us know that the Submit button works
        btnConfirm.setOnClickListener {
            Toast.makeText(context, "Confirm button pressed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNavigation() = with(binding) {
        tvForgotPassword.setOnClickListener {
            navigationCallback.navigate(ForgotPassFragment(), ForgotPassFragment.TAG, TAG)
        }
        setupToolbarNavigation()
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            navigationCallback.popBackStack()
        }
    }

    override fun setButtonAvailability(isEnable: Boolean) {
        binding.btnConfirm.isEnabled = isEnable
    }

    override fun setErrorMessage(inputType: InputTextType, hasError: Boolean) {
        with(binding) {
            when (inputType) {
                InputTextType.EMAIL -> {
                    inputEmailLayout.error = if (hasError) getString(R.string.generic_pass_error_message)
                    else null
                }

                else -> {
                    inputPasswordLayout.error = if (hasError) {
                        inputPasswordLayout.errorIconDrawable = null
                        getString(R.string.generic_pass_error_message)
                    }
                    else null
                }
            }
        }
    }

    companion object {
        const val TAG = "SignInFragment"
    }
}
