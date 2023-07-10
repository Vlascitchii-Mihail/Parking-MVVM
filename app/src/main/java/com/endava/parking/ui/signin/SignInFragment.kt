package com.endava.parking.ui.signin

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
import com.endava.parking.databinding.FragmentSignInBinding
import com.endava.parking.ui.parkinglots.ParkingLotsFragment
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.makeTextClickable
import com.endava.parking.ui.utils.showToast
import com.endava.parking.utils.BlankSpacesInputFilter
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel: SignInViewModel by viewModels()
    private val inputFilter = BlankSpacesInputFilter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupNavigation()
        setupObservers()
    }

    private fun setupView() = with(binding) {
        tvTermsConditions.makeTextClickable(
            phrase = resources.getString(R.string.generic_terms_conditions_phrase),
            phraseColor = R.color.dark_gray,
            font = resources.getFont(R.font.mulish_700),
            onClickListener = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                startActivity(intent)
            }
        )

        inputEmail.filters = arrayOf(inputFilter)
        inputEmail.setOnEditorActionListener { _, actionId, _ ->
            //set focus on inputPassword if was clicked the keyboard next button
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                inputPassword.requestFocus()
            }
            return@setOnEditorActionListener true
        }

        inputPassword.filters = arrayOf(inputFilter)
        inputPassword.setOnEditorActionListener { _, actionId, _ ->
            //hide keyboard if was clicked the keyboard done button from password field
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
            }
            return@setOnEditorActionListener true
        }

        inputEmail.addTextChangedListener {
            viewModel.checkButtonState(
                checkNotEmpty(inputEmail, inputPassword)
            )
        }

        inputPassword.addTextChangedListener {
            viewModel.checkButtonState(
                checkNotEmpty(inputEmail, inputPassword)
            )
        }

        btnConfirm.setOnClickListener {
            viewModel.validateInput(inputEmail.text.toString(), inputPassword.text.toString())
        }
    }

    private fun setupNavigation() = with(binding) {
        tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }
        setupToolbarNavigation()
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.validationStates.observe(viewLifecycleOwner) { validationStateList ->
            setErrorMessage(validationStateList)
        }
        viewModel.buttonEnabled.observe(viewLifecycleOwner) { isValidInput ->
            binding.btnConfirm.isEnabled = isValidInput
        }
        viewModel.showToastEvent.observe(viewLifecycleOwner) { stringId ->
            requireContext().showToast(stringId)
        }
        viewModel.navigateToParkingLots.observe(viewLifecycleOwner) { navigateToParkingLots() }
    }

    private fun navigateToParkingLots() {
        val bundle = Bundle()
        // TODO. Change according to backend
        bundle.putParcelable(ParkingLotsFragment.USER_KEY, User("Name", "w@w.w", "Ab12#", "12345678"))
        findNavController().navigate(R.id.action_signInFragment_to_parkingLotsFragment, bundle)
    }

    private fun setErrorMessage(validationStateList: List<InputState>) {
        with(binding) {
            inputEmailLayout.error =
                if (validationStateList.first().isValid) null
                else resources.getString(validationStateList.first().errorMessage)

            inputPasswordLayout.error = if (validationStateList.last().isValid) null
            else {
                inputPasswordLayout.errorIconDrawable = null
                resources.getString(validationStateList.last().errorMessage)
            }
        }
    }

    private fun checkNotEmpty(emailField: TextInputEditText, passField: TextInputEditText): Boolean {
        return !emailField.text.isNullOrEmpty() && !passField.text.isNullOrEmpty()
    }
}
