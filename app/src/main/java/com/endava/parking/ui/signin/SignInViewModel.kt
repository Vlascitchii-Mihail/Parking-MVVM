package com.endava.parking.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.UserRepository
import com.endava.parking.ui.utils.InputTextType
import com.endava.parking.ui.utils.InputState
import com.endava.parking.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignInViewModel @Inject constructor(
    @Named("Email") private val emailValidator: Validator,
    @Named("Password") private val passwordValidator: Validator,
    private val userRepository: UserRepository
): ViewModel()  {

    private val _navigateToParkingLots = MutableLiveData<String>()   // TODO. Change according to backend
    val navigateToParkingLots: LiveData<String> = _navigateToParkingLots

    private val _validationStates: MutableLiveData<List<InputState>> = MutableLiveData()
    val validationStates: LiveData<List<InputState>>
        get() = _validationStates

    private val _buttonEnabled = MutableLiveData<Boolean>()
    val buttonEnabled: LiveData<Boolean> get() = _buttonEnabled
    private val _showToastEvent = MutableLiveData<Int>()
    val showToastEvent: LiveData<Int> = _showToastEvent


    fun validateInput(emailInput: String, passInput: String) {
        val isValidEmail = emailValidator.validate(emailInput)
        val isValidPass = passwordValidator.validate(passInput)
        _validationStates.value = getInputStateList(isValidEmail, isValidPass)

        if (isValidEmail && isValidPass) {
            signIn(emailInput, passInput)
            _showToastEvent.value = R.string.sign_in_confirm
            _navigateToParkingLots.value = emailInput     // TODO. Change according to backend
        }
    }

    fun checkButtonState(isEmptyFields: Boolean) {
        _buttonEnabled.value = isEmptyFields
    }

    private fun getInputStateList(isValidEmailInput: Boolean = false, isValidPassInput: Boolean = false): List<InputState> {
        return listOf(
            InputState(InputTextType.EMAIL, isValidEmailInput, R.string.generic_email_error_message),
            InputState(InputTextType.PASSWORD, isValidPassInput, R.string.sign_in_pass_error_message)
        )
    }

    private fun signIn(emailInput: String, passInput: String) {
        viewModelScope.launch {
            userRepository.signIn(emailInput, passInput)
        }
    }
}
