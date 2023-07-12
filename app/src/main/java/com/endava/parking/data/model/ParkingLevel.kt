package com.endava.parking.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParkingLevel(
    val name: String,
    val capacity: Int
): Parcelable
