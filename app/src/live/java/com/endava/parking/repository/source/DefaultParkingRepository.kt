package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.User
import javax.inject.Inject

class DefaultParkingRepository @Inject constructor(): ParkingRepository {
    override suspend fun createParkingLot(parkingLot: ParkingLot): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteParkingLot(parkingLot: ParkingLot): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchParkingLots(user: User): Result<List<ParkingLot>> {
        TODO("Not yet implemented")
    }
}
