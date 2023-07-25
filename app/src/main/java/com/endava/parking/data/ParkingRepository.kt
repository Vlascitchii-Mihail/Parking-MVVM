package com.endava.parking.data

import com.endava.parking.data.model.ParkingLot
import retrofit2.Response

interface ParkingRepository {


    suspend fun updateParkingLot(parkingLot: ParkingLot): Response<String>

    suspend fun createParkingLot(parkingLot: ParkingLot): Response<String>

    suspend fun fetchParkingLots(): Response<List<ParkingLot>>

    suspend fun deleteParkingLot(parkingLotName: String): Response<String>

    suspend fun getParkingSpots(token: String?, parkingNme: String, levelName: String): Response<ParkingLot>

    suspend fun takeUpSpot(
        token: String?,
        spotName: String,
        spotType: String,
        parkingLotName: String,
        levelName: String
    ): Response<String>
}
