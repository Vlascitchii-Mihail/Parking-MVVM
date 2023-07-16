package com.endava.parking.ui.tabsparkinglot

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.endava.parking.BaseFragment
import com.endava.parking.data.model.UserRole
import com.endava.parking.ui.createparkinglot.CreateParkingLotViewModel
import com.endava.parking.ui.createparkinglot.FragmentCreateParkingLot
import com.endava.parking.ui.details.DetailsFragment
import com.endava.parking.ui.spotslist.SpotsFragment

class TabPagerAdapter(fragment: Fragment, private val userRole: String = UserRole.ADMIN.role) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int  = TAB_AMOUNT


    override fun createFragment(position: Int): Fragment = when(position) {
        FIRST_TAB -> if (userRole == UserRole.REGULAR.role) DetailsFragment()
        else FragmentCreateParkingLot.newInstance(EDIT_ACTION)
//        else FragmentCreateParkingLot()
        else -> SpotsFragment()
    }

//    override fun createFragment(position: Int): Fragment {
//        val fragment = FragmentCreateParkingLot()
//        BaseFragment.baseInstance(EDIT_ACTION, fragment)
//        return if (position == 0) fragment else SpotsFragment()
//    }

    companion object {
        const val TAB_AMOUNT = 2
        const val FIRST_TAB = 0
        const val EDIT_ACTION = "EDIT ACTION"
    }
}

