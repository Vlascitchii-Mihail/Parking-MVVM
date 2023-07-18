package com.endava.parking.ui.restorepassword

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endava.parking.BaseFragment
import com.endava.parking.databinding.FragmentRestorePassBinding
import com.endava.parking.ui.utils.showToast
import com.endava.parking.utils.BlankSpacesInputFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestorePassFragment : BaseFragment<FragmentRestorePassBinding>(FragmentRestorePassBinding::inflate) {

    private val viewModel: RestorePassViewModel by viewModels()
    private val inputFilter = BlankSpacesInputFilter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        setupToolbarNavigation()
    }

    private fun setupViews() = with(binding) {
        inputEmail.filters = arrayOf(inputFilter)
        inputEmail.addTextChangedListener { viewModel.checkButtonState(inputEmail.text.toString()) }
        btnConfirm.setOnClickListener { viewModel.validateEmail(inputEmail.text.toString()) }
    }

    private fun setupObservers() {
        viewModel.inputState.observe(viewLifecycleOwner) { handleErrorMessage(it) }
        viewModel.buttonEnableState.observe(viewLifecycleOwner) { binding.btnConfirm.isEnabled = it }
        viewModel.resultMessage.observe(viewLifecycleOwner) { requireContext().showToast(resources.getString(it)) }
        viewModel.serverErrorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
    }

    private fun handleErrorMessage(state: EmailFieldState) {
        if (state.isValid) {
            binding.inputEmailLayout.isErrorEnabled = false
            binding.inputEmailLayout.error = null
        } else {
            binding.inputEmailLayout.isErrorEnabled = true
            binding.inputEmailLayout.error = resources.getString(state.errorMessage)
        }
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
