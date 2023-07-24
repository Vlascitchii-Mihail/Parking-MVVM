package com.endava.parking.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

private const val START_HOUR = "06:00"
private const val END_HOUR = "23:00"

@Parcelize
data class ParkingLot(
    @Transient
    var isAdmin: Boolean = false,
    @Expose(serialize = false)
    val id: String = "",
    @Expose
    val name: String = "",
    @Expose
    val address: String = "",
    @Expose
    val startHour: String? = START_HOUR,
    @Expose
    val endHour: String? = END_HOUR,
    @Expose
    val isNonStop: Boolean? = false,
    @Expose
    val isClosed: Boolean? = false,
    @Expose(serialize = false)
    val accessibleParkingSpots: Int = 0,
    @Expose(serialize = false)
    val familyFriendlySpots: Int = 0,
    @Expose(serialize = false)
    val occupiedSeats: Float = 0F,
    @Expose
    val days: List<String> = emptyList(),
    @Expose
    val levels: List<ParkingLevel> = emptyList()
): Parcelable
