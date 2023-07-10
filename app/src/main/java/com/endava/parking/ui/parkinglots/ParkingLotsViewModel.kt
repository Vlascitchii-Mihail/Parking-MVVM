package com.endava.parking.ui.parkinglots

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingLotsViewModel @Inject constructor(
    private val repository: ParkingRepository
) : ViewModel() {

    private val _fetchParkingLots = MutableLiveData<List<ParkingLot>>()
    val fetchParkingLots: LiveData<List<ParkingLot>> = _fetchParkingLots

    private val _fetchListError = MutableLiveData<String>()
    val fetchListError: LiveData<String> = _fetchListError

    private val _openSpotByQrCode = MutableLiveData<String>()
    val openSpotByQrCode: LiveData<String> = _openSpotByQrCode

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    fun fetchParkingLots(user: User) {
        viewModelScope.launch {
            _progressBarVisibility.value = true
            delay(2000)
            val result = repository.fetchParkingLots(user)
            result
                .onSuccess { _fetchParkingLots.value = it }
                .onFailure { _fetchListError.value = it.message }
            _progressBarVisibility.value = false
        }
    }

    fun openSpotByQrCode(qrCode: String) { _openSpotByQrCode.value = qrCode }
}
