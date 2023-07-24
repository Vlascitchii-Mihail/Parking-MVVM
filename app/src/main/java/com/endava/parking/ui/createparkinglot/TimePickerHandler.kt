package com.endava.parking.ui.createparkinglot

import androidx.fragment.app.Fragment
import com.endava.parking.ui.utils.getFormattedTime
import com.endava.parking.ui.utils.getTimePicker

class TimePickerHandler(private val fragment: Fragment) {

    private val startTimePicker = getTimePicker(fragment.requireContext(), FragmentCreateParkingLot.REQUEST_START_TIME)
    private val endTimePicker = getTimePicker(fragment.requireContext(), FragmentCreateParkingLot.REQUEST_END_TIME)
    private val startTimeKeeper = mutableListOf<Int>()
    private val endTimeKeeper = mutableListOf<Int>()
    private var isTimeStatusDisabled: Boolean = true

    fun showTimePicker() {
        startTimePicker.show(
            fragment.childFragmentManager,
            FragmentCreateParkingLot.REQUEST_START_TIME
        )
    }

    fun setTimePickerListener(
        clearFocus: () -> Unit,
        setTime: (timeMap: Map<String, String>) -> Unit
    ){
        with(startTimePicker) {
            addOnPositiveButtonClickListener {
                startTimeKeeper.clear()
                endTimeKeeper.clear()

                startTimeKeeper.add(this.hour)
                startTimeKeeper.add( this.minute)
                endTimePicker.show(fragment.childFragmentManager, FragmentCreateParkingLot.REQUEST_START_TIME)
            }

            addOnNegativeButtonClickListener {
                clearFocus.invoke()
            }
        }

        with(endTimePicker) {
            addOnPositiveButtonClickListener {
                endTimeKeeper.add(this.hour)
                endTimeKeeper.add( this.minute)
                clearFocus.invoke()
                setTime(returnTimeMapped())
            }

            addOnNegativeButtonClickListener {
                clearFocus.invoke()
            }
        }
    }

    /**
     * Return time to fragment
     */
    private fun returnTimeMapped(): Map<String, String> {
        return mapOf(
            FragmentCreateParkingLot.REQUEST_START_TIME to getTime(FragmentCreateParkingLot.REQUEST_START_TIME),
            FragmentCreateParkingLot.REQUEST_END_TIME to getTime(FragmentCreateParkingLot.REQUEST_END_TIME)
        )
    }

    /**
     * Convert integer time to string time like "12:00"
     */
    private fun getTime(timeType: String): String {
        return if (timeType == FragmentCreateParkingLot.REQUEST_START_TIME)
            getFormattedTime(startTimeKeeper.first(), startTimeKeeper.last())
        else getFormattedTime(endTimeKeeper.first(), endTimeKeeper.last())
    }

    /**
     * If start time is greater than end time invalidate inout
     */
    fun getValidationTimeStatus(): Boolean {
        return if (startTimeKeeper.isEmpty() && endTimeKeeper.isEmpty()) true
            else startTimeKeeper.first() < endTimeKeeper.first() || isTimeStatusDisabled
    }

    fun disableTimeValidation(isEnabled: Boolean) {
        this.isTimeStatusDisabled = isEnabled
    }
}
