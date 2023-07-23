package com.endava.parking.ui.tabs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.QrNavigation
import com.endava.parking.databinding.FragmentTabsBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabsFragment : BaseFragment<FragmentTabsBinding>(FragmentTabsBinding::inflate) {

    private val args: TabsFragmentArgs by navArgs()
    private lateinit var intent: NavigationIntent
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var tabPagerAdapter: TabPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        intent = args.intent

        setupView()
        setupToolbarNavigation()

        when (intent.intentType) {
            IntentType.ADMIN_TO_UPDATE -> { adminToUpdate(intent.parking) }
            IntentType.REGULAR_TO_DETAILS -> { regularToDetails(intent.parking) }
            IntentType.REGULAR_TO_SPOTS -> { regularToSpot(intent.qrNavigation) }
        }
    }

    private fun setupView() = with(binding){
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbarParkingTabs)

        tabPagerAdapter = TabPagerAdapter(this@TabsFragment, intent)
        vpgParkingTabs.adapter = tabPagerAdapter

        tabLayoutMediator = TabLayoutMediator(tlParkingTabs, vpgParkingTabs) { tab, position ->
            if (position == TabPagerAdapter.FIRST_TAB) { tab.setText(R.string.tab_details)
            } else { tab.setText(R.string.tab_parking_spots) }
        }
        tabLayoutMediator.attach()
    }

    private fun adminToUpdate(parking: ParkingLot?) {
        binding.toolbarParkingTabs.title = parking?.name
    }

    private fun regularToDetails(parking: ParkingLot?) {
        binding.toolbarParkingTabs.title = parking?.name
    }

    private fun regularToSpot(qrNavigation: QrNavigation?) {
        binding.vpgParkingTabs.currentItem = 1
        binding.toolbarParkingTabs.title = qrNavigation?.parkingLot
    }

    private fun setupToolbarNavigation() {
        binding.toolbarParkingTabs.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
