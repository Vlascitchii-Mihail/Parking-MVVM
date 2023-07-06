package com.endava.parking.ui.details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.SpotType
import com.endava.parking.databinding.FragmentDetailsBinding
import com.endava.parking.ui.utils.colorWorkingDays
import com.endava.parking.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() = with(binding) {
        setupObservers()
        viewModel.getParkingLot(getParkingIdFromParkingList())
    }

    //test function simulate receiving ID from ParkingListFragment and getting Parking item from server using ID
    private fun getParkingIdFromParkingList() = "1"

    private fun showParkingLotDetails(parkingLot: ParkingLot) {
        with(binding) {
            (activity as AppCompatActivity).supportActionBar?.title = parkingLot.name
            tvDetailsAddressField.text = parkingLot.address

            val workHours =
                if (parkingLot.startHour == null && parkingLot.endHour == null) getString(
                    R.string.label_working_hours_24_7
                ) else "${parkingLot.startHour} - ${parkingLot.endHour}"

            tvDetailsWorkingHoursField.text = workHours
            tvDetailsWorkingDaysField.colorWorkingDays(parkingLot.days, Color.RED)
            tvDetailsAccessibleSpotsField.text = getAccessibleSpots(parkingLot.levels)
            tvDetailsAccessibleFamilySpotsField.text = getAccessibleFamilySpots(parkingLot.levels)
            tvDetailsLevelsField.text = parkingLot.levels.size.toString()
        }
    }

    private fun setupObservers() {
        viewModel.parkingLotInstance.observe(viewLifecycleOwner) { parkingLotInstance ->
            showParkingLotDetails(parkingLotInstance)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        viewModel.serverErrorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
    }

    //All created spots
    private fun getAccessibleSpots(levels: List<ParkingLevel>): String {
        var accessibleSpots = INITIAL_VALUE
        levels.forEach { level ->
            accessibleSpots += level.spots.size
        }
        return accessibleSpots.toString()
    }

    //The Family Friendly spots from all levels
    private fun getAccessibleFamilySpots(levels: List<ParkingLevel>): String {
        var familySpots = INITIAL_VALUE
        levels.forEach { level ->
            familySpots += level.spots.filter { it.spotType == SpotType.FAMILY }.size
        }
        return  familySpots.toString()
    }

    companion object {
        const val INITIAL_VALUE = 0
    }
}
