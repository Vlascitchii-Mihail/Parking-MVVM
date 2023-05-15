package com.endava.parking.presentation

import android.os.Bundle
import android.view.View
import com.endava.parking.BaseViewBindingFragment
import com.endava.parking.databinding.FragmentSignUpBinding

class SignUpFragment: BaseViewBindingFragment<FragmentSignUpBinding>(
    FragmentSignUpBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.isEnabled = false
    }
}
