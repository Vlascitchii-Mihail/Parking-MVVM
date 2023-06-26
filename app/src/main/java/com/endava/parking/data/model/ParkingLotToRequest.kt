package com.endava.parking.data.model

class ParkingLotToRequest (
    val name: String,
    val address: String,
    val startHour: String? = null,
    val endHour: String? = null,
    val isNonStop: Boolean? = false,
    val isClosed: Boolean? = false,
    val days: List<String>? = emptyList(),
    val levels: List<ParkingLevelToRequest>
)
