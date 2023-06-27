package com.endava.parking.ui.restorepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.UserRepository
import com.endava.parking.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RestorePassViewModel @Inject constructor(
    private val repository: UserRepository,
    @Named("Email") private val emailValidator: Validator
) : ViewModel() {

    private val _inputState = MutableLiveData<EmailFieldState>()
    val inputState: LiveData<EmailFieldState> = _inputState

    private val _buttonEnableState = MutableLiveData<Boolean>()
    val buttonEnableState: LiveData<Boolean> = _buttonEnableState

    private val _showToastEvent = MutableLiveData<Int>()
    val showToastEvent: LiveData<Int> = _showToastEvent

    fun checkButtonState(email: String) {
        _buttonEnableState.value = email != ""
    }

    fun validateEmail(email: String) {
        setErrorMessage(email)
        if ( emailValidator.validate(email) ) {
            viewModelScope.launch {
                repository.restorePassword(email)
                _showToastEvent.value = R.string.restore_success_notification
            }
        }
    }

    private fun setErrorMessage(email: String) {
        _inputState.value = EmailFieldState(
            isValid = emailValidator.validate(email),
            errorMessage = R.string.generic_email_error_message
        )
    }
}
