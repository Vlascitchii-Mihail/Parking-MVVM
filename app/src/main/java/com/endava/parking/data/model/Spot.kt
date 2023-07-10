package com.endava.parking.data.model

data class Spot(
    val id: Int,
    val spotName: String,
    val spotType: SpotType,
    val busy: Boolean
)
