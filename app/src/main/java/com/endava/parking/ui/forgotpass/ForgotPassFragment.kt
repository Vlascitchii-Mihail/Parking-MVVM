package com.endava.parking.ui.forgotpass

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.endava.parking.BaseViewBindingFragment
import com.endava.parking.databinding.FragmentForgotPasswordBinding
import com.endava.parking.utils.EmailValidator

class ForgotPassFragment : BaseViewBindingFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate), ForgotPassContract.View {

    private val presenter = ForgotPassPresenter(this, EmailValidator())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupToolbarNavigation()
    }

    private fun setupViews() = with(binding) {
        inputEmail.addTextChangedListener {
            presenter.checkUserValidation(inputEmail.text.toString())
        }
    }

    override fun setButtonAvailability(isEnable: Boolean) {
        binding.btnConfirm.isEnabled = isEnable
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            navigationCallback.popBackStack()
        }
    }

    companion object {
        const val TAG = "ForgotPassFragment"
    }
}
