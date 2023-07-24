package com.endava.parking.ui.mockscreen

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.endava.parking.BaseFragment
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.databinding.FragmentMockBinding

class MockFragment : BaseFragment<FragmentMockBinding>(FragmentMockBinding::inflate) {

    private val args: MockFragmentArgs by navArgs()
    private lateinit var parking: ParkingLot

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parking = args.parkinglot
        binding.mockText.text = parking.name
        setupToolbarNavigation()
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
