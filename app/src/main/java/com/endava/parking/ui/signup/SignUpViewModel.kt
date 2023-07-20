package com.endava.parking.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.UserRepository
import com.endava.parking.data.datastore.DefaultAuthDataStore
import com.endava.parking.data.model.User
import com.endava.parking.data.model.UserRole
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.InputTextType
import com.endava.parking.utils.Validator
import com.endava.parking.utils.getUserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @Named("Name") private val nameValidator: Validator,
    @Named("Email") private val emailValidator: Validator,
    @Named("Password") private val passwordValidator: Validator,
    @Named("Phone") private val phoneValidator: Validator,
    private val defaultAuthDataStore: DefaultAuthDataStore
) : ViewModel() {

    private val _navigateToParkingLots = MutableLiveData<UserRole>()
    val navigateToParkingLots: LiveData<UserRole> = _navigateToParkingLots

    private val _inputStates = MutableLiveData<List<InputState>>()
    val inputStates: LiveData<List<InputState>> = _inputStates

    private val _buttonEnableState = MutableLiveData<Boolean>()
    val buttonEnableState: LiveData<Boolean> = _buttonEnableState

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage

    private val _serverErrorMessage = MutableLiveData<String>()
    val serverErrorMessage: LiveData<String> = _serverErrorMessage

    fun validateUser(user: User) {
        setErrorMessageStates(user)
        checkButtonState(user)
        if (nameValidator.validate(user.name) &&
            emailValidator.validate(user.email) &&
            passwordValidator.validate(user.password) &&
            phoneValidator.validate(user.phone)
        ) {
            viewModelScope.launch {
                _buttonEnableState.value = false
                try {
                    val response = userRepository.signUp(user)
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        signIn(user.email, user.password)
                    } else {
                        _serverErrorMessage.value = response.errorBody()?.string()
                    }
                } catch (ex: Exception) {
                    _errorMessage.value = R.string.something_wrong_happened
                    ex.printStackTrace()
                }
                _buttonEnableState.value = true
            }
        }
    }

    private suspend fun signIn(emailInput: String, passInput: String) {
        val response = userRepository.signIn(emailInput, passInput)
        val token = response.body()
        if (response.isSuccessful && token != null) {
            val userRole = getUserRole(token)
            if (userRole == UserRole.INVALID) {
                _errorMessage.value = R.string.decoding_error
                return
            }
            defaultAuthDataStore.putUserRole(userRole.role)
            defaultAuthDataStore.putAuthToken(token = token)
            _navigateToParkingLots.value = userRole
        } else {
            _serverErrorMessage.value = response.errorBody()?.string()
        }
    }

    fun checkButtonState(user: User) {
        _buttonEnableState.value = user.name != ""
            && user.email != ""
            && user.password != ""
            && user.phone != ""
    }

    private fun setErrorMessageStates(user: User) {
        _inputStates.value = listOf(
            InputState(
                fieldType = InputTextType.NAME,
                isValid = nameValidator.validate(user.name),
                errorMessage = chooseNameErrorMessage(user.name)),
            InputState(
                fieldType = InputTextType.EMAIL,
                isValid = emailValidator.validate(user.email),
                errorMessage = R.string.generic_email_error_message),
            InputState(
                fieldType = InputTextType.PASSWORD,
                isValid = passwordValidator.validate(user.password),
                errorMessage = choosePasswordErrorMessage(user.password)),
            InputState(
                fieldType = InputTextType.PHONE,
                isValid = phoneValidator.validate(user.phone),
                errorMessage = choosePhoneErrorMessage(user.phone))
            )
    }

    private fun chooseNameErrorMessage(name: String): Int {
        if (name.length > 30) {
            return R.string.error_message_name_length_higher
        }
        return R.string.error_message_name_non_alpha
    }

    private fun choosePasswordErrorMessage(password: String): Int {
        if (password.length < 5 || password.length > 10) {
            return R.string.generic_pass_error_message
        }
        return R.string.generic_pass_content_error_message
    }

    private fun choosePhoneErrorMessage(phone: String): Int {
        if (phone.length != 8) {
            return R.string.error_message_phone_8_digits
        }
        return R.string.error_message_phone_only_digits
    }
}
