package com.endava.parking.ui.tabsparkinglot

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.endava.parking.ui.details.DetailsFragment
import com.endava.parking.ui.spotslist.SpotsFragment

class TabPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int  = TAB_AMOUNT

    override fun createFragment(position: Int): Fragment = when(position) {
        FIRST_TAB -> DetailsFragment()
        else -> SpotsFragment()
    }

    companion object {
        const val TAB_AMOUNT = 2
        const val FIRST_TAB = 0
    }
}
