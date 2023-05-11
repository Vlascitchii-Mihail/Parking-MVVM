package com.endava.parking.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.endava.parking.BaseViewBindingFragment
import com.endava.parking.R
import com.endava.parking.databinding.FragmentWelcomeScreenBinding

class WelcomeScreenFragment : BaseViewBindingFragment<FragmentWelcomeScreenBinding>(
    FragmentWelcomeScreenBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        Glide.with(activity).load(R.drawable.parking).crossFade().into(binding.imageParkingGif)
    }
}
