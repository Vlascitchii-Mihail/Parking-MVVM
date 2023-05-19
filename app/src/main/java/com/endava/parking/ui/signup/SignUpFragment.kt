package com.endava.parking.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.endava.parking.BaseViewBindingFragment
import com.endava.parking.R
import com.endava.parking.databinding.FragmentSignUpBinding
import com.endava.parking.ui.utils.makeTextClickable

class SignUpFragment : BaseViewBindingFragment<FragmentSignUpBinding>(
    FragmentSignUpBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() = with(binding) {
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
}
