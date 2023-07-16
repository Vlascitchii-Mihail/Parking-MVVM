package com.endava.parking.ui.tabsparkinglot

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.databinding.FragmentParkingLotTabsBinding
import com.google.android.material.tabs.TabLayoutMediator

class ParkingLotTabs : BaseFragment<FragmentParkingLotTabsBinding>(
    FragmentParkingLotTabsBinding::inflate
){

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() = with(binding) {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbarParkingTabs)

        val tabPagerAdapter = TabPagerAdapter(this@ParkingLotTabs, testNavigationGetUserRoleParkingList())
        vpgParkingTabs.adapter = tabPagerAdapter

        tabLayoutMediator = TabLayoutMediator(tlParkingTabs, vpgParkingTabs) { tab, position ->
            if (position == TabPagerAdapter.FIRST_TAB) {
                tab.setText(R.string.tab_details)
            } else {
                tab.setText(R.string.tab_parking_spots)
            }
        }
        tabLayoutMediator.attach()
    }

    //TODO receive from navigation
    private fun testNavigationGetUserRoleParkingList() = "Admin"


    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}
