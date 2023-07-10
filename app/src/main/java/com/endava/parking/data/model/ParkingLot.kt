package com.endava.parking.data.model

private const val START_HOUR = "06:00"
private const val END_HOUR = "23:00"

data class ParkingLot(
    val id: String,
    val name: String,
    val address: String,
    val startHour: String? = START_HOUR,
    val endHour: String? = END_HOUR,
    val isNonStop: Boolean? = false,
    val isClosed: Boolean? = false,
    val days: List<String>? = emptyList(),
    val levels: List<ParkingLevel>,
    val occupancyLevel: Int
)
