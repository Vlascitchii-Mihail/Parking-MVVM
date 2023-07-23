package com.endava.parking.data

import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.ParkingLotToRequest
import com.endava.parking.data.model.Spot
import retrofit2.Response
import com.endava.parking.data.model.User

interface ParkingRepository {

    suspend fun createParkingLot(parkingLot: ParkingLotToRequest): Response<String>

    suspend fun updateParkingLot(parkingLot: ParkingLot): Response<String>

    suspend fun deleteParkingLot(id: String): Response<String>

    suspend fun fetchParkingLots(): Response<List<ParkingLot>>

    suspend fun getParkingSpots(parkingNme: String, levelName: String): Response<List<Spot>>

    suspend fun takeUpSpot(
        token: String?,
        spotName: String,
        spotType: String,
        parkingLotName: String,
        levelName: String
    ): Response<String>
}
