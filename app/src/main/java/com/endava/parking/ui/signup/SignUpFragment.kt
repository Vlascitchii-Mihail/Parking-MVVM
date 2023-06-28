package com.endava.parking.ui.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.User
import com.endava.parking.databinding.FragmentSignUpBinding
import com.endava.parking.ui.utils.makeTextClickable
import com.endava.parking.ui.utils.showToast
import com.endava.parking.utils.BlankSpacesInputFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()
    private val inputFilter = BlankSpacesInputFilter()
    private lateinit var signUpErrorHandler: SignUpErrorHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.inputStates.observe(viewLifecycleOwner) { signUpErrorHandler.setErrorStates(it) }
        viewModel.buttonEnableState.observe(viewLifecycleOwner) { binding.btnConfirm.isEnabled = it }
        viewModel.showToastEvent.observe(viewLifecycleOwner) { requireContext().showToast(resources.getString(it)) }
    }

    private fun setupViews() = with(binding) {
        signUpErrorHandler = SignUpErrorHandler(binding)
        btnConfirm.setOnClickListener { viewModel.validateUser(getUser()) }

        inputPhone.addTextChangedListener { viewModel.checkButtonState(getUser()) }

        inputName.filters = arrayOf(inputFilter)
        inputName.addTextChangedListener { viewModel.checkButtonState(getUser()) }

        inputEmail.filters = arrayOf(inputFilter)
        inputEmail.addTextChangedListener { viewModel.checkButtonState(getUser()) }

        inputPassword.filters = arrayOf(inputFilter)
        inputPassword.addTextChangedListener { viewModel.checkButtonState(getUser()) }

        /**  On NEXT action, transfer focus to phone field, over Show password icon  */
        inputPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                inputPhone.requestFocus()
            }
            true
        }
        tvAgreementMessage.makeTextClickable(
            phrase = resources.getString(R.string.generic_terms_conditions_phrase),
            phraseColor = R.color.dark_gray,
            font = resources.getFont(R.font.mulish_700),
            onClickListener = {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")))
            }
        )
        setupToolbarNavigation()
    }

    private fun getUser(): User = User(
        binding.inputName.text.toString(),
        binding.inputEmail.text.toString(),
        binding.inputPassword.text.toString(),
        binding.inputPhone.text.toString()
    )

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
