package com.endava.parking.ui.welcomescreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.endava.parking.BaseViewBindingFragment
import com.endava.parking.R
import com.endava.parking.databinding.FragmentWelcomeScreenBinding
import com.endava.parking.ui.signin.SignInFragment
import com.endava.parking.ui.signup.SignUpFragment

class WelcomeFragment : BaseViewBindingFragment<FragmentWelcomeScreenBinding>(
    FragmentWelcomeScreenBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireActivity()).load(R.drawable.parking).into(binding.imageParkingGif)
        setupNavigation()
    }

    private fun setupNavigation() = with(binding) {
        val click = { fragment: Fragment, tag: String ->
            navigationCallback.navigate(fragment, tag)
        }

        btnSignIn.setOnClickListener { click(SignInFragment(), SignInFragment.TAG) }
        btnCreateAccount.setOnClickListener { click(SignUpFragment(), SignUpFragment.TAG) }
    }
}
