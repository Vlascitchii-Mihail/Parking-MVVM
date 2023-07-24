package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.api.ApiService
import com.endava.parking.data.datastore.AuthDataStore
import com.endava.parking.data.model.ParkingLot
import retrofit2.Response
import javax.inject.Inject

class DefaultParkingRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: AuthDataStore
) : ParkingRepository {

    override suspend fun createParkingLot(parkingLot: ParkingLot): Response<String> =
        apiService.createParkingLot("Bearer ${dataStore.getAuthToken()}", parkingLot)

    override suspend fun updateParkingLot(parkingLot: ParkingLot): Response<String> =
        apiService.updateParkingLot(parkingLot)

    override suspend fun fetchParkingLots(): Response<List<ParkingLot>> =
        apiService.fetchParkingLots("Bearer ${dataStore.getAuthToken()}")

    override suspend fun deleteParkingLot(token: String, parkingLotName: String): Response<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getParkingSpots(
        token: String?,
        parkingNme: String,
        levelName: String
    ): Response<ParkingLot> = apiService.getParkingSpots(dataStore.getAuthToken(), parkingNme, levelName)

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
