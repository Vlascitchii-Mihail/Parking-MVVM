package com.endava.parking.ui.details

import android.os.Parcelable
import com.endava.parking.data.model.UserRole
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParkingDetails(
    val userRole: UserRole = UserRole.INVALID,
    val token: String = "",
    val parkingId: String = "0",
    val parkingName: String = "ParkingName",
    val parkingAddress: String = "",
    val workingHours: String = "",
    val isNonStop: Boolean = false,
    val workingDays: List<String> = emptyList(),
    val accessibleSpotsNumber: Int = 0,
    val familySpotsNumber: Int = 0,
    val levelsNumber: Int = 0,
    val qrParkingLevel: String = "",
    val qrSpotName: String = ""
): Parcelable
