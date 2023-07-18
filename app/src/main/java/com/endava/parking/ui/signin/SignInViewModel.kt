package com.endava.parking.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.UserRepository
import com.endava.parking.data.datastore.DefaultAuthDataStore
import com.endava.parking.data.model.UserRole
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.InputTextType
import com.endava.parking.ui.utils.SingleEventLiveData
import com.endava.parking.utils.Validator
import com.endava.parking.utils.getUserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignInViewModel @Inject constructor(
    @Named("Email") private val emailValidator: Validator,
    @Named("Password") private val passwordValidator: Validator,
    private val userRepository: UserRepository,
    private val defaultAuthDataStore: DefaultAuthDataStore
): ViewModel()  {

    private val _navigateToParkingLots = MutableLiveData<String>()   // TODO. Change according to backend
    val navigateToParkingLots: LiveData<String> = _navigateToParkingLots

    private val _validationStates: MutableLiveData<List<InputState>> = MutableLiveData()
    val validationStates: LiveData<List<InputState>> get() = _validationStates

    private val _buttonEnabled = MutableLiveData<Boolean>()
    val buttonEnabled: LiveData<Boolean> get() = _buttonEnabled

    private val _errorMessage = SingleEventLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage

    private val _serverErrorMessage = SingleEventLiveData<String>()
    val serverErrorMessage: LiveData<String> = _serverErrorMessage

    fun validateInput(emailInput: String, passInput: String) {
        val isValidEmail = emailValidator.validate(emailInput)
        val isValidPass = passwordValidator.validate(passInput)
        _validationStates.value = getInputStateList(isValidEmail, isValidPass)

        if (isValidEmail && isValidPass) {
            signIn(emailInput, passInput)
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
            try {
                val response = userRepository.signIn(emailInput, passInput)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val userRole = getUserRole(body)
                    if (userRole == UserRole.INVALID) {
                        _errorMessage.value = R.string.decoding_error
                        return@launch
                    }
                    defaultAuthDataStore.putUserRole(userRole = userRole.role)
                    defaultAuthDataStore.putAuthToken(token = body)
                    _navigateToParkingLots.value = UserRole.REGULAR.role
                } else _serverErrorMessage.value = response.errorBody()?.string()
            } catch (ex: Exception) {
                _errorMessage.value = R.string.something_wrong_happened
                ex.printStackTrace()
            }
        }
    }
}
