package com.endava.parking.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.UserRepository
import com.endava.parking.data.model.User
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.InputTextType
import com.endava.parking.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository,
    @Named("Name") private val nameValidator: Validator,
    @Named("Email") private val emailValidator: Validator,
    @Named("Password") private val passwordValidator: Validator,
    @Named("Phone") private val phoneValidator: Validator
) : ViewModel() {

    private val _inputStates = MutableLiveData<List<InputState>>()
    val inputStates: LiveData<List<InputState>> = _inputStates

    private val _buttonEnableState = MutableLiveData<Boolean>()
    val buttonEnableState: LiveData<Boolean> = _buttonEnableState

    private val _showToastEvent = MutableLiveData<Int>()
    val showToastEvent: LiveData<Int> = _showToastEvent

    fun validateUser(user: User) {
        setErrorMessageStates(user)
        checkButtonState(user)
        if (nameValidator.validate(user.name) &&
            emailValidator.validate(user.email) &&
            passwordValidator.validate(user.password) &&
            phoneValidator.validate(user.phone)
        ) {
            viewModelScope.launch {
                repository.signUp(user)
                _showToastEvent.value = R.string.signup_user_was_submit
            }
        }
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

    fun checkButtonState(user: User) {
        _buttonEnableState.value = user.name != ""
            && user.email != ""
            && user.password != ""
            && user.phone != ""
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
