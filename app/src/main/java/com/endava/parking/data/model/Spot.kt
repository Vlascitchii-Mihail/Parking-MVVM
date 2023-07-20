package com.endava.parking.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Spot(
    val id: Int,
    val spotName: String,
    val spotType: SpotType,
    val busy: Boolean
): Parcelable
