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
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ParkingLotsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val parkingRepository: ParkingRepository
) : ViewModel() {

    private lateinit var parkingList: List<ParkingLot>

    private val _fetchParkingLots = MutableLiveData<List<ParkingLot>>()
    val fetchParkingLots: LiveData<List<ParkingLot>> = _fetchParkingLots

    private val _setUserRole = MutableLiveData<UserRole>()
    val setUserRole: LiveData<UserRole> = _setUserRole

    private val _serverErrorMessage = MutableLiveData<String>()
    val serverErrorMessage: LiveData<String> = _serverErrorMessage

    private val _openSpotByQrCode = MutableLiveData<QrNavigation>()
    val openSpotByQrCode: LiveData<QrNavigation> = _openSpotByQrCode

    private val _qrCodeError = MutableLiveData<Int>()
    val qrCodeError: LiveData<Int> = _qrCodeError

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    private val _httpException = MutableLiveData<String>()
    val httpException: LiveData<String> = _httpException

    fun fetchParkingLots(token: String) {
        viewModelScope.launch {
            _progressBarVisibility.value = true
            try {
                /** Put token for test only */
                authDataStore.putAuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6ImQxMDM1N2E2LTRlYjEtNDI0Ny05MmRkLTEyOTU4YmIyYTY5YiIsIlVzZXJOYW1lIjoiQW5hdG9saWUiLCJFbWFpbCI6InN0YW1idWxvQHlhbmRleC5ydSIsIlJvbGUiOiJVc2VyIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoiVXNlciIsImV4cCI6MTY5MTg2MjMyNywiaXNzIjoiUGFya2luZ1BsYW5uZXIiLCJhdWQiOiJQYXJraW5Mb2dVSSJ9.js3RzHw3JmVjilGixxQy_NhHe6jBPQe51enf4cZt6IM")
                val response = parkingRepository.fetchParkingLots(checkNotNull(authDataStore.getAuthToken()))
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    _fetchParkingLots.value = checkNotNull(body)
                    parkingList = body
                } else {
                    _serverErrorMessage.value = response.message()
                }
            } catch (e: HttpException) { _httpException.value = e.localizedMessage }
            catch (e: Exception) { _httpException.value = e.localizedMessage }
            _progressBarVisibility.value = false
        }
    }

    fun searchParking(text: String) {
        _fetchParkingLots.value = parkingList.filter { s -> s.name.lowercase().contains( text.lowercase() ) } }

    fun openSpotByQrCode(qrCode: String) {
        var json: JSONObject? = null
        val gson = Gson()
        try {
            json = JSONObject(qrCode)
        } catch (e: Exception) {e.printStackTrace()}
        when (json) {
            is JSONObject -> { _openSpotByQrCode.value = gson.fromJson(qrCode, QrNavigation::class.java) }
            else -> { _qrCodeError.value = R.string.parking_lot_wrong_qr_error_message }
        }
    }
}
