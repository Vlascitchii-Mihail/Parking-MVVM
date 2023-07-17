package com.endava.parking.ui.tabsparkinglot

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.FragmentParkingLotTabsBinding
import com.google.android.material.tabs.TabLayoutMediator

class ParkingLotTabs : BaseFragment<FragmentParkingLotTabsBinding>(
    FragmentParkingLotTabsBinding::inflate
){

    private lateinit var tabLayoutMediator: TabLayoutMediator
    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null
    private var trashHolderItem: MenuItem? = null
    private lateinit var onPageChangeCallback:  ViewPager2.OnPageChangeCallback
    private lateinit var tabPagerAdapter:  TabPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() = with(binding) {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbarParkingTabs)

        setupMenu()

        tabPagerAdapter = TabPagerAdapter(this@ParkingLotTabs, testNavigationGetUserRoleParkingList())
        with(vpgParkingTabs) {
            adapter = tabPagerAdapter
            onPageChangeCallback = getOnPageChangeCallback()
            vpgParkingTabs.registerOnPageChangeCallback(onPageChangeCallback)
        }

        tabLayoutMediator = TabLayoutMediator(tlParkingTabs, vpgParkingTabs) { tab, position ->
            if (position == TabPagerAdapter.FIRST_TAB) tab.setText(R.string.tab_details)
            else tab.setText(R.string.tab_parking_spots)
        }
        tabLayoutMediator.attach()


        toolbarParkingTabs.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.parking_lot_menu, menu)
                searchItem = menu.findItem(R.id.search_parking_spots)
                trashHolderItem = menu.findItem(R.id.menu_delete_parking_lot)
                searchView = searchItem?.actionView as SearchView

                setupSearch()

                customiseSearchView(menu)
                setSearchViewVisibility(true)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete_parking_lot -> {}//TODO add code
                }
                return false
            }

        }, viewLifecycleOwner)
    }

    private fun customiseSearchView(menu: Menu) {
        //set custom font
        val searchText = searchView?.findViewById<View>(androidx.appcompat.R.id.search_src_text) as TextView
        searchText.typeface = resources.getFont(R.font.mulish_400)

        //set drawable resource background
        menu.findItem(R.id.search_parking_spots).actionView?.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.searchview_background)
    }

    private fun setSearchViewVisibility(isVisible: Boolean) {
        searchItem?.isVisible = !isVisible
        trashHolderItem?.isVisible = isVisible && testNavigationGetUserRoleParkingList() == UserRole.ADMIN.role
    }

    private fun getOnPageChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            setSearchViewVisibility(TabPagerAdapter.FIRST_TAB == position)
        }
    }

    private fun setupSearch() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { tabPagerAdapter.searchSpot(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { tabPagerAdapter.searchSpot(it) }
                return false
            }

        })
    }

    //test function
    private fun testNavigationGetUserRoleParkingList() = "User"


    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
        binding.vpgParkingTabs.unregisterOnPageChangeCallback(onPageChangeCallback)
    }
}
