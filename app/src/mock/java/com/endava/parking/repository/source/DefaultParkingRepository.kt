package com.endava.parking.repository.source

import com.endava.parking.data.ParkingRepository
import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.ParkingLotToRequest
import com.endava.parking.data.model.Spot
import com.endava.parking.data.model.SpotType
import com.endava.parking.data.model.User
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
                "Wednesday",
                "Thursday",
                "Friday"
            ),
            levels = listOf(
                ParkingLevel(
                    "Level A", 60, arrayListOf(Spot(0, "A-001", SpotType.REGULAR, false))
                )
            ),
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
                ParkingLevel("Level A", 60, arrayListOf(
                    Spot(0, "A-001", SpotType.FAMILY, false),
                    Spot(0, "A-002", SpotType.DISABLED_PERSON, false),
                    Spot(0, "A-003", SpotType.REGULAR, false),
                    Spot(0, "A-004", SpotType.FAMILY, true),
                    Spot(0, "A-005", SpotType.TEMPORARY_CLOSED, false),
                    Spot(0, "A-006", SpotType.DISABLED_PERSON, false),
                    Spot(0, "A-007", SpotType.REGULAR, false),
                    Spot(0, "A-008", SpotType.FAMILY, true),
                    Spot(0, "A-009", SpotType.REGULAR, false),
                    Spot(0, "A-010", SpotType.TEMPORARY_CLOSED, false),
                    Spot(0, "A-011", SpotType.DISABLED_PERSON, true),
                    Spot(0, "A-012", SpotType.FAMILY, false),
                    Spot(0, "A-013", SpotType.REGULAR, false),
                    Spot(0, "A-014", SpotType.REGULAR, true),
                    Spot(0, "A-015", SpotType.TEMPORARY_CLOSED, false),
                    Spot(0, "A-016", SpotType.FAMILY, false),
                    Spot(0, "A-017", SpotType.TEMPORARY_CLOSED, false),
                    Spot(0, "A-018", SpotType.DISABLED_PERSON, true),
                    Spot(0, "A-019", SpotType.FAMILY, true),
                    Spot(0, "A-020", SpotType.TEMPORARY_CLOSED, false),
                    Spot(0, "A-021", SpotType.REGULAR, false),
                    Spot(0, "A-022", SpotType.FAMILY, false),
                    Spot(0, "A-023", SpotType.REGULAR, true),
                    Spot(0, "A-024", SpotType.REGULAR, true),
                    Spot(0, "A-025", SpotType.FAMILY, false),
                )),
                ParkingLevel("Level B", 40, arrayListOf(
                    Spot(1, "B-001", SpotType.FAMILY, false),
                    Spot(1, "B-002", SpotType.DISABLED_PERSON, false),
                    Spot(1, "B-003", SpotType.TEMPORARY_CLOSED, false),
                    Spot(1, "B-004", SpotType.REGULAR, true),
                    Spot(1, "B-005", SpotType.REGULAR, false),
                    Spot(1, "B-006", SpotType.FAMILY, false),
                    Spot(1, "B-007", SpotType.DISABLED_PERSON, true),
                    Spot(1, "B-008", SpotType.FAMILY, false),
                    Spot(1, "B-009", SpotType.TEMPORARY_CLOSED, false),
                    Spot(1, "B-010", SpotType.REGULAR, true),
                    Spot(1, "B-011", SpotType.FAMILY, false),
                    Spot(1, "B-012", SpotType.DISABLED_PERSON, false),
                    Spot(1, "B-013", SpotType.FAMILY, true),
                    Spot(1, "B-014", SpotType.REGULAR, false),
                    Spot(1, "B-015", SpotType.REGULAR, true),
                    Spot(1, "B-016", SpotType.FAMILY, false),
                    Spot(1, "B-017", SpotType.TEMPORARY_CLOSED, false),
                    Spot(1, "B-018", SpotType.REGULAR, false),
                    Spot(1, "B-019", SpotType.DISABLED_PERSON, true),
                    Spot(1, "B-020", SpotType.REGULAR, true),
                    Spot(1, "B-021", SpotType.DISABLED_PERSON, false),
                    Spot(1, "B-022", SpotType.FAMILY, false),
                    Spot(1, "B-023", SpotType.REGULAR, false),
                    Spot(1, "B-024", SpotType.TEMPORARY_CLOSED, false),
                    Spot(1, "B-025", SpotType.REGULAR, false),
                )),
            ),
            occupancyLevel = 65,
        ),
        ParkingLot(
            id = "2",
            name = "Kaufland Parking Lot",
            address = "Some Address",
            isNonStop = true,
            levels = listOf(
                ParkingLevel("Level A", 120, arrayListOf(Spot(0, "A-001", SpotType.REGULAR, false)))
            ),
            occupancyLevel = 85
        ),
        ParkingLot(
            id = "3",
            name = "N1 Hypermarket Parking Lot",
            address = "Some Address",
            isClosed = true,
            levels = listOf(
                ParkingLevel("Level A", 80, arrayListOf(Spot(0, "A-001", SpotType.REGULAR, false)))
            ),
            occupancyLevel = 0
        )
    )

    override suspend fun createParkingLot(
        token: String?,
        parkingLot: ParkingLotToRequest
    ): Response<String> {
        return Response.success(parkingLot.name)
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

    override suspend fun getParkingSpots(
        token: String?,
        parkingNme: String,
        levelName: String
    ): Response<ParkingLot> {
        return Response.success(this.data.filter { it.name == parkingNme }.first())
    }

    override suspend fun getParkingLotDescription(token: String?, parkingId: String): Response<ParkingLot> {
         return Response.success(this.data.filter { it.id == parkingId }.first())
    }

    override suspend fun takeUpSpot(
        token: String?,
        spotName: String,
        spotType: String,
        parkingLotName: String,
        levelName: String
    ): Response<String> {
        return Response.success("User has booked the parking spot")
    }

    override suspend fun getParkingLot(token: String?, parkingLotId: String): Response<ParkingLot> {
        return Response.success(data.filter { it.id == parkingLotId }.first())
    }
}
