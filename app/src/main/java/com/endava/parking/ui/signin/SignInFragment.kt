package com.endava.parking.ui.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.endava.parking.BaseViewBindingFragment
import com.endava.parking.R
import com.endava.parking.databinding.FragmentSignInScreenBinding
import com.endava.parking.ui.utils.makeTextClickable

class SignInFragment : BaseViewBindingFragment<FragmentSignInScreenBinding>(
    FragmentSignInScreenBinding::inflate
) {

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

        setupToolbarNavigation()
    }

    private fun setupNavigation() = with(binding) {
        tvForgotPassword.setOnClickListener {
            //todo: navigate to ForgotPasswordFragment
        // navigationCallback.navigate(ForgotPasswordFragment(), TAG)
        }
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            navigationCallback.popBackStack()
        }
    }

    companion object {
        const val TAG = "SignInFragment"
    }
}
