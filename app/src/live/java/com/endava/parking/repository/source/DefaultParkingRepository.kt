package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.api.ApiService
import com.endava.parking.data.model.ParkingLot
import retrofit2.Response
import javax.inject.Inject

class DefaultParkingRepository @Inject constructor(
    private val apiService: ApiService
): ParkingRepository {
    override suspend fun createParkingLot(parkingLot: ParkingLot): Result<String> =
        apiService.createParkingLot(parkingLot)

    override suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String> =
        apiService.updateParkingLot(parkingLot)

    override suspend fun deleteParkingLot(id: String): Result<String> =
        apiService.deleteParkingLot(id)

    override suspend fun fetchParkingLots(token: String): Response<List<ParkingLot>> =
        apiService.fetchParkingLots("Bearer $token")

}
