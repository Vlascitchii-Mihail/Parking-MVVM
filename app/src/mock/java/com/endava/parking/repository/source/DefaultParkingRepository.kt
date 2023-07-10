package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.User
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
        )
    )

    override suspend fun createParkingLot(parkingLot: ParkingLot): Result<String> {
        return Result.success("Parking ${parkingLot.name} was create")
    }

    override suspend fun updateParkingLot(parkingLot: ParkingLot): Result<String> {
        return Result.success("Parking ${parkingLot.name} was updated")
    }

    override suspend fun deleteParkingLot(parkingLot: ParkingLot): Result<String> {
        return Result.success("Parking ${parkingLot.name} was deleted")
    }

    override suspend fun fetchParkingLots(user: User): Result<ArrayList<ParkingLot>> {
        return Result.success(data)
    }
}
