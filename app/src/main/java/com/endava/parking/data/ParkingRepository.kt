package com.endava.parking.data

import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.User

interface ParkingRepository {

    suspend fun createParkingLot(parkingLot: ParkingLot): Result<String>

    suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String>

    suspend fun deleteParkingLot(parkingLot: ParkingLot): Result<String>

    suspend fun fetchParkingLots(user: User): Result<List<ParkingLot>>
}
