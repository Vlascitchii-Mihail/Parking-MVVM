package com.endava.parking.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.endava.parking.BaseViewBindingFragment
import com.endava.parking.R
import com.endava.parking.data.User
import com.endava.parking.databinding.FragmentSignUpBinding
import com.endava.parking.ui.utils.makeTextClickable
import com.endava.parking.utils.EmailValidator
import com.endava.parking.utils.NameValidator
import com.endava.parking.utils.PasswordValidator
import com.endava.parking.utils.PhoneValidator

class SignUpFragment : BaseViewBindingFragment<FragmentSignUpBinding>(
    FragmentSignUpBinding::inflate
), SignUpContract.View {

    private val presenter = SignUpPresenter(this,
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
}
