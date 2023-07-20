package com.endava.parking.ui.tabsparkinglot

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.endava.parking.BaseFragment
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.FragmentParkingLotTabsBinding

class ParkingLotTabs : BaseFragment<FragmentParkingLotTabsBinding>(
    FragmentParkingLotTabsBinding::inflate
){

    private val args: ParkingLotTabsArgs by navArgs()
    private lateinit var userRole: UserRole

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRole = args.role
        setupView()
        setupToolbarNavigation()
    }

    private fun setupView() = with(binding) {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbarParkingTabs)
        binding.mockText.text = "User role - $userRole"
    }

    private fun setupToolbarNavigation() {
        binding.toolbarParkingTabs.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
