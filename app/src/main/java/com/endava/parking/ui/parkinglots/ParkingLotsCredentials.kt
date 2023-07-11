package com.endava.parking.ui.parkinglots

import android.os.Parcelable
import com.endava.parking.data.model.UserRole
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParkingLotsCredentials(
    val token: String ="",
    val userRole: UserRole = UserRole.INVALID
): Parcelable
