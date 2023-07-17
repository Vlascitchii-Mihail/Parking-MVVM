package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.api.ApiService
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.ParkingLotToRequest
import com.endava.parking.data.model.User
import retrofit2.Response
import javax.inject.Inject

class DefaultParkingRepository @Inject constructor(
    private val apiService: ApiService
): ParkingRepository {

    override suspend fun createParkingLot(token: String?, parkingLot: ParkingLotToRequest): Response<String> =
        apiService.createParkingLot("Bearer $token", parkingLot)

    override suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteParkingLot(parkingLot: ParkingLot): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchParkingLots(user: User): Result<List<ParkingLot>> {
        TODO("Not yet implemented")
    }

    override suspend fun getParkingSpots(token: String?, parkingNme: String, levelName: String): Response<ParkingLot> {
        return apiService.getParkingSpots(token, parkingNme, levelName)
    }

    override suspend fun getParkingLotDescription(token: String?, parkingId: String): Response<ParkingLot> {
        return apiService.getParkingLotDescription(token, parkingId)
    }

    override suspend fun takeUpSpot(
        token: String?,
        spotName: String,
        spotType: String,
        parkingLotName: String,
        levelName: String
    ): Response<String> {
        return apiService.takeUpSpot(token, mapOf("name" to spotName, "typeName" to spotType, "parkingName" to parkingLotName, "levelName" to levelName))
    }

    override suspend fun getParkingLot(token: String?, parkingLotId: String): Response<ParkingLot> {
        TODO("Not yet implemented")
    }
}
