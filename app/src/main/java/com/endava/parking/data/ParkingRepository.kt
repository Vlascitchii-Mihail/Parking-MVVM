package com.endava.parking.data

import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.ParkingLotToRequest
import com.endava.parking.data.model.User
import retrofit2.Response

interface ParkingRepository {

    suspend fun createParkingLot(token: String?, parkingLot: ParkingLotToRequest): Response<String>

    suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String>

    suspend fun deleteParkingLot(parkingLot: ParkingLot): Result<String>

    suspend fun fetchParkingLots(user: User): Result<List<ParkingLot>>
}
