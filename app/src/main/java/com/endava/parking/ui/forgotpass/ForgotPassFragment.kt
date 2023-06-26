package com.endava.parking.ui.forgotpass

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.endava.parking.BaseFragment
import com.endava.parking.databinding.FragmentForgotPasswordBinding
import com.endava.parking.utils.EmailValidator

class ForgotPassFragment : BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate), ForgotPassContract.View {

    private val presenter = ForgotPassPresenter(this, EmailValidator())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupToolbarNavigation()
    }

    override fun onStart() {
        super.onStart()
        setOnKeyboardCloseListener {
            if (binding.inputEmail.text.toString() != "") {
                presenter.checkUserValidation(binding.inputEmail.text.toString())
            }
        }
    }

    private fun setupViews() = with(binding) {
        // test code lets us know that the Submit button works
        btnConfirm.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Was pressed Submit button in Forgot Password Fragment",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun setButtonAvailability(isEnable: Boolean) {
        binding.btnConfirm.isEnabled = isEnable
    }

    override fun showErrorMessage(messageId: Int) {
        binding.inputEmailLayout.isErrorEnabled = true
        binding.inputEmailLayout.error = resources.getString(messageId)
    }

    override fun clearErrorMessage() {
        binding.inputEmailLayout.isErrorEnabled = false
        binding.inputEmailLayout.error = null
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
