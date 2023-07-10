package com.endava.parking.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    var userRole: UserRole = UserRole.REGULAR
) : Parcelable
