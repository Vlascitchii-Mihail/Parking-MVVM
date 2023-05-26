package com.endava.parking.ui.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.endava.parking.BaseViewBindingFragment
import com.endava.parking.R
import com.endava.parking.databinding.FragmentSignInBinding
import com.endava.parking.ui.forgotpass.ForgotPassFragment
import com.endava.parking.ui.utils.makeTextClickable
import com.endava.parking.utils.EmailValidator
import com.endava.parking.utils.PasswordValidator

class SignInFragment : BaseViewBindingFragment<FragmentSignInBinding>(
    FragmentSignInBinding::inflate
), SignInContract.View {

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
        inputEmail.addTextChangedListener {
            validateUserInput()
        }
        inputPassword.addTextChangedListener {
            validateUserInput()
        }

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

        setupToolbarNavigation()
    }

    private fun setupNavigation() = with(binding) {
        tvForgotPassword.setOnClickListener {
         navigationCallback.navigate(ForgotPassFragment(), ForgotPassFragment.TAG)
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

    private fun validateUserInput() {
        with(binding) {
            presenter.checkUserValidation(
                inputEmail.text.toString(),
                inputPassword.text.toString()
            )
        }
    }

    companion object {
        const val TAG = "SignInFragment"
    }
}
