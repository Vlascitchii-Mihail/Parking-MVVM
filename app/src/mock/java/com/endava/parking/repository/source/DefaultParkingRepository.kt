package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.data.model.ParkingLot
import retrofit2.Response
import javax.inject.Inject

class DefaultParkingRepository @Inject constructor(): ParkingRepository {

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
                "Thursday",
                "Friday"
            ),
            levels = listOf(ParkingLevel("Level A", 60)),
            occupiedSeats = 63.77777F
        ),
        ParkingLot(
            id = "1",
            name = "Endava 777",
            address = "Some Address",
            startHour = "10:00",
            endHour = "23:00",
            days = listOf(
                "Monday",
                "Tuesday",
                "Wednesday"
            ),
            levels = listOf(
                ParkingLevel("Level A", 60),
                ParkingLevel("Level B", 40)
            ),
            occupiedSeats = 19.28F
        ),
        ParkingLot(
            id = "2",
            name = "Kaufland Parking Lot",
            address = "Some Address",
            isNonStop = true,
            levels = listOf(
                ParkingLevel("Level A", 120)
            ),
            occupiedSeats = 265.888885F
        ),
        ParkingLot(
            id = "3",
            name = "N2 Hypermarket Parking Lot",
            address = "Some Address",
            isClosed = true,
            levels = listOf(
                ParkingLevel("Level A", 80)
            ),
            occupiedSeats = 10.3F
        ),
        ParkingLot(
            id = "4",
            name = "Endava Tower North",
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
            occupiedSeats = 57.4545F,
        ),
        ParkingLot(
            id = "5",
            name = "METRO Cahul",
            address = "Some Address",
            startHour = "08:00",
            endHour = "20:00",
            days = listOf(
                "Monday",
                "Wednesday",
                "Friday",
                "Sunday"
            ),
            levels = listOf(
                ParkingLevel("Level A", 120)
            ),
            occupiedSeats = 28.94748F
        ),
        ParkingLot(
            id = "6",
            name = "Terminal Parking Lot",
            address = "Some Address",
            startHour = "10:00",
            endHour = "23:00",
            days = listOf(
                "Monday",
                "Tuesday",
                "Wednesday"
            ),
            levels = listOf(
                ParkingLevel("Level A", 60),
                ParkingLevel("Level B", 40)
            ),
            occupiedSeats = 29.28F
        ),
        ParkingLot(
            id = "7",
            name = "Victoria Parking Lot",
            address = "Some Address",
            isNonStop = true,
            levels = listOf(
                ParkingLevel("Level A", 120)
            ),
            occupiedSeats = 82.888885F
        ),
        ParkingLot(
            id = "8",
            name = "N3 Hypermarket Parking Lot",
            address = "Some Address",
            isClosed = true,
            levels = listOf(
                ParkingLevel("Level A", 80)
            ),
            occupiedSeats = 0.7F
        ),
        ParkingLot(
            id = "9",
            name = "Plaza Palace",
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
            occupiedSeats = 47.4545F,
        ),
        ParkingLot(
            id = "10",
            name = "Fourchette-1 Parking",
            address = "Some Address",
            startHour = "08:00",
            endHour = "20:00",
            days = listOf(
                "Monday",
                "Wednesday",
                "Friday",
                "Sunday"
            ),
            levels = listOf(
                ParkingLevel("Level A", 120)
            ),
            occupiedSeats = 7F
        ),
        ParkingLot(
            id = "11",
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
                ParkingLevel("Level A", 60),
                ParkingLevel("Level B", 40)
            ),
            occupiedSeats = 70.28F
        ),
        ParkingLot(
            id = "12",
            name = "Kaufland",
            address = "Some Address",
            isNonStop = true,
            levels = listOf(
                ParkingLevel("Level A", 120)
            ),
            occupiedSeats = 69.888885F
        ),
        ParkingLot(
            id = "13",
            name = "N1 Hypermarket Parking Lot",
            address = "Some Address",
            startHour = "10:00",
            endHour = "23:00",
            days = listOf(
                "Monday",
                "Tuesday",
                "Friday",
                "Thursday"
            ),
            levels = listOf(
                ParkingLevel("Level A", 80)
            ),
            occupiedSeats = 33F
        ),
        ParkingLot(
            id = "14",
            name = "Endava London",
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
            occupiedSeats = 32.4545F,
        ),
        ParkingLot(
            id = "15",
            name = "METRO Cahul",
            address = "Some Address",
            startHour = "08:00",
            endHour = "20:00",
            days = listOf(
                "Monday",
                "Wednesday",
                "Friday",
                "Sunday"
            ),
            levels = listOf(
                ParkingLevel("Level A", 120)
            ),
            occupiedSeats = 330F
        ),
        ParkingLot(
            id = "16",
            name = "Terminal-12 Parking Lot",
            address = "Some Address",
            startHour = "10:00",
            endHour = "23:00",
            days = listOf(
                "Monday",
                "Tuesday",
                "Wednesday"
            ),
            levels = listOf(
                ParkingLevel("Level A", 60),
                ParkingLevel("Level B", 40)
            ),
            occupiedSeats = 16.28F
        ),
        ParkingLot(
            id = "17",
            name = "Victoria Parking Lot",
            address = "Some Address",
            isNonStop = true,
            levels = listOf(
                ParkingLevel("Level A", 120)
            ),
            occupiedSeats = 85.888885F
        ),
        ParkingLot(
            id = "18",
            name = "N5 Hypermarket Parking Lot",
            address = "Some Address",
            startHour = "10:00",
            endHour = "23:00",
            days = listOf(
                "Monday",
                "Tuesday",
                "Wednesday"),
            levels = listOf(
                ParkingLevel("Level A", 80)
            ),
            occupiedSeats = 22F
        ),
        ParkingLot(
            id = "19",
            name = "Plaza Balti Palace",
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
            occupiedSeats = 57.4545F,
        ),
        ParkingLot(
            id = "20",
            name = "Fourchette-2 Parking",
            address = "Some Address",
            startHour = "08:00",
            endHour = "20:00",
            days = listOf(
                "Monday",
                "Wednesday",
                "Friday",
                "Sunday"
            ),
            levels = listOf(
                ParkingLevel("Level A", 120)
            ),
            occupiedSeats = 31.22223F
        )
    )

    override suspend fun createParkingLot(parkingLot: ParkingLot): Response<String> {
        return Response.success("Parking ${parkingLot.name} was create")
    }

    override suspend fun updateParkingLot(parkingLot: ParkingLot): Response<String> {
        return Response.success("Parking ${parkingLot.name} was updated")
    }

    override suspend fun deleteParkingLot(parkingLotId: String): Response<String> {
        return Response.success("Parking $parkingLotId was deleted")
    }

    override suspend fun fetchParkingLots(): Response<List<ParkingLot>> {
        return Response.success(data)
    }

    override suspend fun getParkingSpots(
        token: String?,
        parkingNme: String,
        levelName: String
    ): Response<ParkingLot> {
        return Response.success(this.data.first { it.name == parkingNme })
    }

    override suspend fun takeUpSpot(
        token: String?,
        spotName: String,
        spotType: String,
        parkingLotName: String,
        levelName: String
    ): Response<String> {
        return Response.success("User took a place")
    }
}
