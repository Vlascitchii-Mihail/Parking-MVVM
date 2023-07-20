package com.endava.parking.ui.parkinglots

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.R
import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.datastore.AuthDataStore
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.QrNavigation
import com.endava.parking.data.model.UserRole
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingLotsViewModel @Inject constructor(
    private val parkingRepository: ParkingRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    private val _fetchParkingLots = MutableLiveData<List<ParkingLot>>()
    val fetchParkingLots: LiveData<List<ParkingLot>> = _fetchParkingLots

    private val _serverErrorMessage = MutableLiveData<String>()
    val serverErrorMessage: LiveData<String> = _serverErrorMessage

    private val _openSpotByQrCode = MutableLiveData<QrNavigation>()
    val openSpotByQrCode: LiveData<QrNavigation> = _openSpotByQrCode

    private val _qrCodeError = MutableLiveData<Int>()
    val qrCodeError: LiveData<Int> = _qrCodeError

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    fun fetchParkingLots() {
        viewModelScope.launch {
            val role = UserRole.getFromString(authDataStore.getUserRole())
            _progressBarVisibility.value = true
            try {
                val response = parkingRepository.fetchParkingLots()
                val parkingLots = response.body()
                if (response.isSuccessful && parkingLots != null) {
                    _fetchParkingLots.value = parkingLots.sortedBy { it.name }.map {
                        it.isAdmin = role == UserRole.ADMIN
                        it
                    }
                } else {
                    _serverErrorMessage.value = response.message()
                }
            } catch (e: Exception) {
                _serverErrorMessage.value = e.localizedMessage
            }
            _progressBarVisibility.value = false
        }
    }

    fun openSpotByQrCode(qrCode: String) {
        try {
            _openSpotByQrCode.value = Gson().fromJson(qrCode, QrNavigation::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            _qrCodeError.value = R.string.parking_lot_wrong_qr_error_message
        }
    }
}
