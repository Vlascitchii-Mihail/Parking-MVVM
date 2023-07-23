package com.endava.parking.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QrNavigation(
    val parkingLot: String = "",
    val level: String = "",
    val parkingSpot: Int = 0
) : Parcelable
