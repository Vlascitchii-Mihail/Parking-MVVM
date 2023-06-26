package com.endava.parking.ui.utils

import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.data.model.ParkingLevelToRequest
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.ParkingLotToRequest

fun ParkingLot.toRequest() = ParkingLotToRequest(
    this.name,
    this.address,
    this.startHour,
    this.endHour,
    this.isNonStop,
    this.isClosed,
    this.days,
    this.levels.toParkingLevelRequest()
)

fun ParkingLevel.toRequest() = ParkingLevelToRequest(
    this.name,
    this.capacity
)

fun List<ParkingLevel>.toParkingLevelRequest(): List<ParkingLevelToRequest> {
    val levels: MutableList<ParkingLevelToRequest> = mutableListOf()
    this.forEach { level ->
        levels.add(level.toRequest())
    }
    return levels.toList()
}
