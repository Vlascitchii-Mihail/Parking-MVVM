package com.endava.parking.ui.welcomescreen

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.databinding.FragmentWelcomeScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment: BaseFragment<FragmentWelcomeScreenBinding>(
    FragmentWelcomeScreenBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(R.drawable.parking).into(binding.imageParkingGif)
        setupNavigation()
    }

    private fun setupNavigation() = with(binding) {
        btnSignIn.setOnClickListener { navController.navigate(R.id.action_welcomeScreenFragment_to_SignInFragment) }
        btnCreateAccount.setOnClickListener { navController.navigate(R.id.action_welcomeScreenFragment_to_signUpFragment) }
    }
}

