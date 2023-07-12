package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.data.model.ParkingLot
import retrofit2.Response
import javax.inject.Inject

class DefaultParkingRepository @Inject constructor() : ParkingRepository {

    /**
     *    Mock Parking List
     */
    val data: ArrayList<ParkingLot> = arrayListOf(
        ParkingLot(
            id = "0",
            name = "Llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogochdfsfgkjgdffdqwertyuioplkflyflyflyfly",
            address = "Some Address",
            startHour = "18:00",
            endHour = "08:00",
            days = listOf(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday"
            ),
            levels = listOf(ParkingLevel("A", 60)),
            occupancyLevel = 28
        ),
        ParkingLot(
            id = "1",
            name = "Endava Tower Parking Lot",
            address = "Some Address",
            startHour = "10:00",
            endHour = "23:00",
            days = listOf(
                "Monday",
                "Tuesday",
                "Wednesday"
            ),
            levels = listOf(
                ParkingLevel("A", 60),
                ParkingLevel("B", 40)
            ),
            occupancyLevel = 65,
        ),
        ParkingLot(
            id = "2",
            name = "Kaufland Parking Lot",
            address = "Some Address",
            isNonStop = true,
            levels = listOf(ParkingLevel("A", 120)),
            occupancyLevel = 85
        ),
        ParkingLot(
            id = "3",
            name = "N1 Hypermarket Parking Lot",
            address = "Some Address",
            isClosed = true,
            levels = listOf(ParkingLevel("A", 80)),
            occupancyLevel = 0
        ),
        ParkingLot(
            id = "4",
            name = "Endava Tower",
            address = "Some Address",
            startHour = "10:00",
            endHour = "23:00",
            days = listOf(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday"
            ),
            levels = listOf(
                ParkingLevel("A", 60),
                ParkingLevel("B", 40),
                ParkingLevel("C", 40)
            ),
            occupancyLevel = 23,
        )
    )

    override suspend fun createParkingLot(parkingLot: ParkingLot): Result<String> {
        return Result.success("Parking ${parkingLot.name} was create")
    }

    override suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String> {
        return Result.success("Parking ${parkingLot.name} was updated")
    }

    override suspend fun deleteParkingLot(id: String): Response<String> {
        return Response.success("Parking was deleted")
    }

    override suspend fun fetchParkingLots(token: String): Response<List<ParkingLot>> {
        return Response.success(data)
    }
}
