package com.endava.parking.data

import com.endava.parking.data.model.ParkingLot
import retrofit2.Response

interface ParkingRepository {

    suspend fun createParkingLot(parkingLot: ParkingLot): Result<String>

    suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String>

    suspend fun deleteParkingLot(id: String): Response<String>

    suspend fun fetchParkingLots(token: String): Response<List<ParkingLot>>
}
