package com.endava.parking.repository

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.User

class DefaultParkingRepository : ParkingRepository {
    override suspend fun createParkingLot(parkingLot: ParkingLot) {
        TODO("Not yet implemented")
    }

    override suspend fun updateParkingLot(parkingLot: ParkingLot) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteParkingLot(parkingLot: ParkingLot) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchParkingLots(user: User) {
        TODO("Not yet implemented")
    }
}
