package com.endava.parking.ui.spotslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.datastore.DefaultAuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotsViewModel @Inject constructor(
    private val parkingRepository: ParkingRepository,
    private val defaultAuthDataStore: DefaultAuthDataStore
): ViewModel() {

    private var _parkingLotInstance: MutableLiveData<ParkingLot> = MutableLiveData()
    val parkingLotInstance: LiveData<ParkingLot> get() = _parkingLotInstance

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    private val _serverErrorMessage = MutableLiveData<String>()
    val serverErrorMessage: LiveData<String> get() = _serverErrorMessage
    lateinit var lotDescription: List<String>

    fun getParkingSpots() {
        viewModelScope.launch {
            try {
                val token = defaultAuthDataStore.getAuthToken()
                val response = parkingRepository.getParkingSpots(token, lotDescription.first(), lotDescription.last())
                //returns nullable type ParkingLot?
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    //without nullable ParkingLot
                    body.let { parkingLot ->
                        _parkingLotInstance.value = parkingLot
                    }
                } else _serverErrorMessage.value = response.errorBody()?.string()
            } catch (ex: Exception) {
                _errorMessage.value = R.string.something_wrong_happened
                ex.printStackTrace()
            }
        }
    }

    fun takeUpSpot(spotName: String, spotType: String, parkingLotName: String, levelName: String, showResponseMessage: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val token = defaultAuthDataStore.getAuthToken()
                val response = parkingRepository.takeUpSpot(token, spotName, spotType, parkingLotName, levelName)
                //returns nullable type String?
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    //without nullable String?
                    response.body()?.let { responseMessage ->
                        showResponseMessage(responseMessage)
                    }
                } else _serverErrorMessage.value = response.errorBody()?.string()
            } catch (ex: Exception) {
                _errorMessage.value = R.string.something_wrong_happened
                ex.printStackTrace()
            }
        }
    }

    //test function
    fun getUerRole(): String {
        var userRole = "User"
        viewModelScope.launch {
            defaultAuthDataStore.getUserRole()?.let { userRole = it }
        }
        //todo add delay?
        return userRole
    }
}
