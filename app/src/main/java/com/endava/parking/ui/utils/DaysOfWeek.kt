package com.endava.parking.ui.utils

enum class DaysOfWeek constructor(
    val dayOfWeek: Int,
    var printableName: String) {
    SUNDAY(1, "Sunday"),
    MONDAY(2, "Monday"),
    TUESDAY(3, "Tuesday"),
    WEDNESDAY(4, "Wednesday"),
    THURSDAY(5, "Thursday"),
    FRIDAY(6, "Friday"),
    SATURDAY(7, "Saturday");
}
