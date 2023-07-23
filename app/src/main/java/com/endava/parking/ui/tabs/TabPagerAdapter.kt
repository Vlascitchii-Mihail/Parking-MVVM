package com.endava.parking.ui.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.endava.parking.ui.createparkinglot.FragmentCreateParkingLot
import com.endava.parking.ui.details.DetailsFragment
import com.endava.parking.ui.spotslist.SpotDetails
import com.endava.parking.ui.spotslist.SpotsFragment

class TabPagerAdapter(fragment: Fragment, private val intent: NavigationIntent) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when(position) {

            FIRST_TAB -> {
                when (intent.intentType) {
                    IntentType.ADMIN_TO_UPDATE -> {
                        FragmentCreateParkingLot.newInstance(checkNotNull(intent.parking))
                    } else -> {
                        DetailsFragment.newInstance(checkNotNull(intent.parking))
                    }
                }
            }

            else -> SpotsFragment.newInstance(
                SpotDetails(
                    checkNotNull(intent.parking?.name),
                    checkNotNull(intent.parking?.levels)
                )
            )
        }
    }

    override fun getItemCount(): Int  = TAB_AMOUNT

    companion object {
    const val TAB_AMOUNT = 2
    const val FIRST_TAB = 0
    }
}
