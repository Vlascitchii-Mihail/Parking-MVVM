package com.endava.parking.data.model

data class WorkingDay(
    val weekDayNumber: Int,  /**  1-Sunday ... 7-Saturday  */
    var isWorking: Boolean
)
