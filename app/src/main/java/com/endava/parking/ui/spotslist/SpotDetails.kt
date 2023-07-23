package com.endava.parking.ui.spotslist

import android.os.Parcelable
import com.endava.parking.data.model.ParkingLevel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpotDetails(
    val parkingName: String,
    val parkingLevel: List<ParkingLevel>,
): Parcelable
