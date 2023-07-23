package com.endava.parking.ui.details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.endava.parking.BaseFragment
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.databinding.FragmentDetailsBinding
import com.endava.parking.ui.utils.colorWorkingDays
import com.endava.parking.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var parking: ParkingLot

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parking = arguments?.getParcelable(PARKING_KEY)!!  // For test
        setupView()
        setupObservers()
    }

    private fun setupView() {
        with (binding) {
            (activity as AppCompatActivity).supportActionBar?.title = parking.name

            tvDetailsAddressField.text = parking.address
            tvDetailsWorkingHoursField.text = "${parking.startHour} - ${parking.endHour}"
            parking.isNonStop?.let { tvDetailsWorkingDaysField.colorWorkingDays(parking.days, Color.RED, it) }
            tvDetailsAccessibleSpotsField.text = parking.accessibleParkingSpots.toString()
            tvDetailsAccessibleFamilySpotsField.text = parking.familyFriendlySpots.toString()
            tvDetailsLevelsField.text = parking.levels.size.toString()
        }
    }

    private fun setupObservers() {
        viewModel.parkingLotInstance.observe(viewLifecycleOwner) { }
        viewModel.errorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        viewModel.serverErrorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
    }

    companion object {
        private const val PARKING_KEY = "parking_key"

        fun newInstance(parkingLot: ParkingLot) = DetailsFragment().apply {
            arguments = bundleOf(PARKING_KEY to parkingLot)
        }
    }
}
