package com.endava.parking.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

private const val START_HOUR = "06:00"
private const val END_HOUR = "23:00"

@Parcelize
data class ParkingLot(
    @Transient
    var isAdmin: Boolean = false,
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val startHour: String? = START_HOUR,
    val endHour: String? = END_HOUR,
    val isNonStop: Boolean? = false,
    val isClosed: Boolean? = false,
    val accessibleParkingSpots: Int = 0,
    val familyFriendlySpots: Int = 0,
    val occupiedSeats: Float = 0F,
    val days: List<String> = emptyList(),
    val levels: List<ParkingLevel> = emptyList(),
): Parcelable
