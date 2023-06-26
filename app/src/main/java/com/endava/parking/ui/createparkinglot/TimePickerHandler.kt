package com.endava.parking.ui.createparkinglot

import android.content.Context
import com.endava.parking.R
import com.endava.parking.ui.utils.getFormattedTime
import com.endava.parking.ui.utils.getTimePicker
import com.google.android.material.timepicker.MaterialTimePicker

class TimePickerHandler(private val context: Context) {

    private val startTimePicker = getTimePicker(context, FragmentCreateParkingLot.REQUEST_START_TIME)
    private val endTimePicker = getTimePicker(context, FragmentCreateParkingLot.REQUEST_END_TIME)

    fun getStartPicker() = startTimePicker
    fun getEndPicker() = endTimePicker

    fun setTimePickerListener(
        showEndTimePicker: () -> Unit,
        saveTime: (Map<String, List<Int>>) -> Unit,
        setConfirmBtnAvailabilityState: () -> Unit,
        clearFocus: () -> Unit,
        inputTime: (String) -> Unit
    ) {
        with(startTimePicker) {
            addOnPositiveButtonClickListener {
                showEndTimePicker.invoke()

                saveTime(getTime(this, FragmentCreateParkingLot.REQUEST_START_TIME))
            }

            addOnNegativeButtonClickListener {
                clearFocus.invoke()
            }
        }

        with(endTimePicker) {
            addOnPositiveButtonClickListener {
                saveTime(getTime(this, FragmentCreateParkingLot.REQUEST_END_TIME))
                setOperatingHoursPeriod(getStartEndTime(), setConfirmBtnAvailabilityState, inputTime)
                clearFocus.invoke()
            }

            addOnNegativeButtonClickListener {
                clearFocus.invoke()
            }
        }
    }

    private fun getTime(picker: MaterialTimePicker, timeType: String): Map<String, List<Int>> {
        val hour = picker.hour
        val minute = picker.minute
        return mapOf(timeType to listOf(hour, minute))
    }

    private fun setOperatingHoursPeriod(workTime: List<String>, setConfirmBtnAvailabilityState: () -> Unit, inputTime: (String) -> Unit) {
        inputTime(context.getString(R.string.lot_admin_operating_hours, workTime.first(), workTime.last()))
            setConfirmBtnAvailabilityState.invoke()
    }

    private fun getStartEndTime() = listOf(
        getFormattedTime(startTimePicker.hour, startTimePicker.minute),
        getFormattedTime(endTimePicker.hour, endTimePicker.minute)
    )
}
