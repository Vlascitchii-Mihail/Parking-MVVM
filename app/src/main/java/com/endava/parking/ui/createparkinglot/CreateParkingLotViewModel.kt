package com.endava.parking.ui.createparkinglot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.datastore.DefaultAuthDataStore
import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.InputTextType
import com.endava.parking.ui.utils.SingleEventLiveData
import com.endava.parking.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CreateParkingLotViewModel @Inject constructor(
    @Named("LotDetails") private val lotDetailsValidator: Validator,
    @Named("LevelsCapacity") private val levelsCapacityValidator: Validator,
    private val parkingRepository: ParkingRepository,
    private val defaultAuthDataStore: DefaultAuthDataStore
) : ViewModel() {

    var startTime: String? = null
    var endTime: String? = null

    lateinit var levelsErrorIndexes: MutableList<Int>

    private val _validationStates: MutableLiveData<List<InputState>> = MutableLiveData()
    val validationStates: LiveData<List<InputState>>
        get() = _validationStates

    private val _buttonEnabled = MutableLiveData<Boolean>()
    val buttonEnabled: LiveData<Boolean> get() = _buttonEnabled

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage

    private val _navigateBack = SingleEventLiveData<String>()
    val navigateBack: LiveData<String> = _navigateBack

    fun updateButtonState(isEmptyFields: Boolean) {
        _buttonEnabled.value = isEmptyFields
    }

    fun validateInput(
        lotName: String,
        lotAddress: String,
        levels: MutableList<ParkingLevel>,
        isNonStop: Boolean,
        tempClosed: Boolean,
        checkedDays: List<String>,
        isValidTImeRange: Boolean
    ) {
        val isValidParkingName = lotDetailsValidator.validate(lotName)
        val isValidParkingAddress = lotDetailsValidator.validate(lotAddress)
        levelsErrorIndexes = getLevelsFieldsErrorIndexList(levels)

        _validationStates.value = getInputState(
            isValidParkingName,
            isValidParkingAddress,
            isValidTImeRange
        )

        if (isValidParkingName
            && isValidParkingAddress
            && levelsErrorIndexes.isEmpty()
            && isValidTImeRange
        ) {
            createParkingLot(
                ParkingLot(
                    name = lotName,
                    address = lotAddress,
                    startHour = startTime,
                    endHour = endTime,
                    isNonStop = isNonStop,
                    isClosed = tempClosed,
                    days = checkedDays,
                    levels = levels,
                )
            )
        }
    }

    //return indices of fields with error data
    //validation each dynamic field
    private fun getLevelsFieldsErrorIndexList(levelsData: MutableList<ParkingLevel>): MutableList<Int> {
        val fieldsErrorIndex: MutableList<Int> = mutableListOf()
        levelsData.forEachIndexed { index, element ->
            if (!levelsCapacityValidator.validate(
                element.capacity.toString()
                )
            ) fieldsErrorIndex.add(index + 1)
        }
        return fieldsErrorIndex
    }

    //get correctness of the input list
    private fun getInputState(
        isValidParkingName: Boolean,
        isValidParkingAddress: Boolean,
        isValidTime: Boolean,
    ): List<InputState> {
        return listOf(
            InputState(InputTextType.NAME, isValidParkingName, R.string.lot_admin_error),
            InputState(InputTextType.ADDRESS, isValidParkingAddress, R.string.lot_admin_error),
            InputState(InputTextType.TIME, isValidTime, R.string.lot_admin_time_delimitation)
        )
    }

    private fun createParkingLot(parkingLot: ParkingLot) {
        viewModelScope.launch {
            try {
                val response = parkingRepository.createParkingLot(parkingLot)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    _navigateBack.value = response.code().toString()
                } else {
                     _navigateBack.value = response.errorBody()?.string()
                }
            } catch (ex: Exception) {
                _errorMessage.value = R.string.something_wrong_happened
                ex.printStackTrace()
            }
        }
    }
}
