package com.endava.parking.ui.createparkinglot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.datastore.DefaultAuthDataStore
import com.endava.parking.data.model.ParkingLevelToRequest
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.ParkingLotToRequest
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.InputTextType
import com.endava.parking.ui.utils.getFormattedTime
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

    var startTime: List<Int> = emptyList()
    var endTime: List<Int> = emptyList()

    lateinit var levelsErrorIndexes: MutableList<Int>

    private val _validationStates: MutableLiveData<List<InputState>> = MutableLiveData()
    val validationStates: LiveData<List<InputState>>
        get() = _validationStates

    private val _buttonEnabled = MutableLiveData<Boolean>()
    val buttonEnabled: LiveData<Boolean> get() = _buttonEnabled

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage

    private val _serverErrorMessage = MutableLiveData<String>()
    val serverErrorMessage: LiveData<String> = _serverErrorMessage

    private var _parkingLotInstance: MutableLiveData<ParkingLot> = MutableLiveData()
    val parkingLotInstance: LiveData<ParkingLot> get() = _parkingLotInstance

    fun checkButtonState(isEmptyFields: Boolean) {
        _buttonEnabled.value = isEmptyFields
    }

    fun validateInput(
        lotName: String,
        lotAddress: String,
        lotLevelASpots: String,
        levelsData: MutableList<ParkingLevelToRequest>,
        isNonStop: Boolean,
        tempClosed: Boolean,
        checkedDays: List<String>
    ) {
        val isValidParkingName = lotDetailsValidator.validate(lotName)
        val isValidParkingAddress = lotDetailsValidator.validate(lotAddress)
        val isValidLotLevelASpots = levelsCapacityValidator.validate(lotLevelASpots)
        val isValidTime = validateWorkingTime()
        levelsErrorIndexes = getLevelsFieldsErrorIndexList(levelsData)

        _validationStates.value = getInputStateList(
            isValidParkingName,
            isValidParkingAddress,
            isValidLotLevelASpots,
            isValidTime
        )

        if (isValidParkingName
            && isValidParkingAddress
            && isValidLotLevelASpots
            && levelsErrorIndexes.isEmpty()
            && isValidTime
        ) {
            createParkingLot(
                ParkingLotToRequest(
                    lotName,
                    lotAddress,
                    if (startTime.isNotEmpty()) getFormattedTime(
                        startTime.first(),
                        startTime.last()
                    ) else null,
                    if (endTime.isNotEmpty()) getFormattedTime(
                        endTime.first(),
                        endTime.last()
                    ) else null,
                    isNonStop,
                    tempClosed,
                    checkedDays,
                    levelsData,
                )
            )
        }
    }

    //return indices of fields with error data
    //validation each dynamic field
    private fun getLevelsFieldsErrorIndexList(levelsData: MutableList<ParkingLevelToRequest>): MutableList<Int> {
        val fieldsErrorIndex: MutableList<Int> = mutableListOf()
        levelsData.forEachIndexed { index, element ->
            if (!levelsCapacityValidator.validate(
                element.capacity.toString()
                )
            ) fieldsErrorIndex.add(index)
        }
        return fieldsErrorIndex
    }

    //get correctness of the input list
    private fun getInputStateList(
        isValidParkingName: Boolean,
        isValidParkingAddress: Boolean,
        isValidLotLevelASpots: Boolean,
        isValidTime: Boolean,
    ): List<InputState> {
        return listOf(
            InputState(InputTextType.NAME, isValidParkingName, R.string.lot_admin_error),
            InputState(InputTextType.ADDRESS, isValidParkingAddress, R.string.lot_admin_error),
            InputState(InputTextType.SPOTS, isValidLotLevelASpots, R.string.lot_admin_error_levels),
            InputState(InputTextType.TIME, isValidTime, R.string.lot_admin_time_delimitation)
        )
    }

    private fun createParkingLot(parkingLotToRequest: ParkingLotToRequest) {
        viewModelScope.launch {
            try {
                val response = parkingRepository.createParkingLot(
                    defaultAuthDataStore.getAuthToken(),
                    parkingLotToRequest
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    //TODO navigate to ParkingLotList
                } else _serverErrorMessage.value = response.errorBody()?.string()
            } catch (ex: Exception) {
                _errorMessage.value = R.string.something_wrong_happened
                ex.printStackTrace()
            }
        }
    }

    fun getParkingLot(parkingLotId: String) {
        viewModelScope.launch {
            try {
                val response = parkingRepository.getParkingLot(defaultAuthDataStore.getAuthToken(), parkingLotId)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    body.let { _parkingLotInstance.value = it }
                } else _serverErrorMessage.value = response.errorBody()?.string()
            } catch (ex: Exception) {
                _errorMessage.value = R.string.something_wrong_happened
                ex.printStackTrace()
            }
        }
    }

    private fun validateWorkingTime(): Boolean {
        return if (startTime.isNotEmpty() && endTime.isNotEmpty()) startTime.first() < endTime.first()
        else true
    }
}
