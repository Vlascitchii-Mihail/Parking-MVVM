package com.endava.parking.ui.createparkinglot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import com.endava.parking.R
import com.endava.parking.databinding.DaysCheckboxLayoutBinding
import com.endava.parking.ui.utils.DayName

class CheckDaysView(private val context: Context, attr: AttributeSet? = null) : ConstraintLayout(context, attr) {

    private val binding = DaysCheckboxLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    private val checkboxList = getDaysCheckBoxList()

    fun setDaysCheckBoxesAvailability(isActive: Boolean) {
        checkboxList.forEach { day ->
            if (isActive) day.isChecked = false
            day.isEnabled = !isActive
        }
    }

    fun setCheckBoxListeners(setConfirmBtnAvailabilityState: () -> Unit) {
        checkboxList.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                setConfirmBtnAvailabilityState.invoke()
            }
        }
    }

    private fun getDaysCheckBoxList(): List<AppCompatCheckBox> {
        with(binding) {
            return listOf(
                chbParkingSunday,
                chbParkingMonday,
                chbParkingTuesday,
                chbParkingWednesday,
                chbParkingThursday,
                chbParkingFriday,
                chbParkingSaturday
            )
        }
    }

    fun getCheckedDaysString(): List<String> {
        return getDaysStringList(
            checkboxList.filter { it.isChecked } )
    }

    private fun getDaysStringList(dayCheckboxList: List<AppCompatCheckBox>): MutableList<String> {
        val dayList = mutableListOf<String>()
        with(context) {
            dayCheckboxList.forEach { dayCheckbox ->
                when (dayCheckbox.id) {
                    R.id.chb_parking_sunday -> dayList.add(getString(DayName.SUNDAY.dayId))
                    R.id.chb_parking_monday -> dayList.add(getString(DayName.MONDAY.dayId))
                    R.id.chb_parking_tuesday -> dayList.add(getString(DayName.TUESDAY.dayId))
                    R.id.chb_parking_wednesday -> dayList.add(getString(DayName.WEDNESDAY.dayId))
                    R.id.chb_parking_thursday -> dayList.add(getString(DayName.THURSDAY.dayId))
                    R.id.chb_parking_friday -> dayList.add(getString(DayName.FRIDAY.dayId))
                    R.id.chb_parking_saturday -> dayList.add(getString(DayName.SATURDAY.dayId))
                }
            }
        }

        return dayList
    }

    fun checkCheckboxesTicked(isCheckedNonStop: Boolean) = (isCheckedNonStop || getCheckedDaysCheckBoxes())

    private fun getCheckedDaysCheckBoxes(): Boolean {
        return checkboxList.any { it.isChecked }
    }
}
