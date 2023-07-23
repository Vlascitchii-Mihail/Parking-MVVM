package com.endava.parking.ui.tabs

import android.os.Parcelable
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.QrNavigation
import com.endava.parking.data.model.UserRole
import kotlinx.parcelize.Parcelize

@Parcelize
data class NavigationIntent(
    val intentType: IntentType,
    val parking: ParkingLot? = null,
    val qrNavigation: QrNavigation? = null
) : Parcelable
