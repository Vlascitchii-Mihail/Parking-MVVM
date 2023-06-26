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
}
