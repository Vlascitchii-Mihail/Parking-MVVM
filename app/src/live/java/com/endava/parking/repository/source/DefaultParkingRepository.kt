package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.api.ApiService
import com.endava.parking.data.datastore.AuthDataStore
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.ParkingLotToRequest
import com.endava.parking.data.model.Spot
import retrofit2.Response
import javax.inject.Inject

class DefaultParkingRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: AuthDataStore
) : ParkingRepository {

    override suspend fun createParkingLot(parkingLot: ParkingLotToRequest): Response<String> =
        apiService.createParkingLot("Bearer ${dataStore.getAuthToken()}", parkingLot)

    override suspend fun updateParkingLot(parkingLot: ParkingLot): Response<String> =
        apiService.updateParkingLot(parkingLot)

    override suspend fun deleteParkingLot(id: String): Response<String> =
        apiService.deleteParkingLot(id)

    override suspend fun fetchParkingLots(): Response<List<ParkingLot>> =
        apiService.fetchParkingLots("Bearer ${dataStore.getAuthToken()}")

    override suspend fun getParkingSpots(
        parkingNme: String,
        levelName: String
    ): Response<List<Spot>> = apiService.getParkingSpots(dataStore.getAuthToken(), parkingNme, levelName)

    override suspend fun takeUpSpot(
        token: String?,
        spotName: String,
        spotType: String,
        parkingLotName: String,
        levelName: String
    ): Response<String> {
        return apiService.takeUpSpot(dataStore.getAuthToken(), mapOf(
            "name" to spotName,
            "typeName" to spotType,
            "parkingName" to parkingLotName,
            "levelName" to levelName)
        )
    }
}
