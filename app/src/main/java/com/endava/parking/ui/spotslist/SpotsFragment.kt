package com.endava.parking.ui.spotslist

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.data.model.Spot
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.FragmentSpotsDetailsBinding
import com.endava.parking.ui.utils.SpotBottomSheet
import com.endava.parking.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpotsFragment : BaseFragment<FragmentSpotsDetailsBinding>(
    FragmentSpotsDetailsBinding::inflate
) {

    private val viewModel: SpotsViewModel by viewModels()
    private lateinit var spotsListAdapter: SpotsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.lotDescription = getParkingIdFromParkingList()
        viewModel.getParkingSpots()
        setupView()
    }

    // test function simulate receiving ID from ParkingListFragment and getting
    // the Parking item from the server using ID
    private fun getParkingIdFromParkingList() = listOf("Endava Tower Parking Lot", "Level A")

    private fun setupView() {
        setObservers()
        setupRecyclerView()
        viewModel.parkingLotInstance.value?.let { parkingLot ->
            setupSpinnerAppearance(parkingLot.levels)
            setupSpinner(parkingLot.levels)
        }
    }

    private fun setupSpinnerAppearance(levels: List<ParkingLevel>) = with(binding) {
        spnLevels.visibility = if (levels.size == 1)  {
            spotsListAdapter.submitList(levels.first().spots)
            View.GONE
        }
        else View.VISIBLE
    }

    private fun setupSpinner(levels: List<ParkingLevel>) = with(binding){
        val levelsMap = getLevelsName(levels)
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            levelsMap.keys.toList()
        )
        spnLevels.adapter = spinnerAdapter
        setupSpinnerAction(levelsMap.values.toList())
    }

    private fun setupSpinnerAction(levelsList: List<List<Spot>>) = with(binding) {
        spnLevels.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spotsListAdapter.submitList(levelsList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getLevelsName(levels: List<ParkingLevel>): MutableMap<String, List<Spot>> {
        val levelsName = mutableMapOf<String, List<Spot>>()
        levels.forEach { level ->
            levelsName[level.name] = level.spots
        }
        return levelsName
    }

    private fun setupRecyclerView() = with(binding){
        rvTabSpotsList.layoutManager = GridLayoutManager(requireContext(), 3)
        spotsListAdapter = SpotsAdapter { spot: Spot ->
            createBottomSheetDialog(spot, viewModel.lotDescription.first(), viewModel.lotDescription.last())
        }
        rvTabSpotsList.adapter = spotsListAdapter
        setGridSpacing(rvTabSpotsList)
    }

    //sets the spacing between items in a RecyclerView
    private fun setGridSpacing(recyclerView: RecyclerView) {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.margin_spots_rv_grid)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.left = spacingInPixels / HORIZONTAL_SPACING_MODIFIER
                outRect.right = spacingInPixels / HORIZONTAL_SPACING_MODIFIER
                outRect.top = spacingInPixels / VERTICAL_SPACING_MODIFIER
                outRect.bottom = spacingInPixels / VERTICAL_SPACING_MODIFIER
            }
        })
        recyclerView.setPadding(
            spacingInPixels / - HORIZONTAL_SPACING_MODIFIER,
            spacingInPixels / - VERTICAL_SPACING_MODIFIER,
            spacingInPixels / - HORIZONTAL_SPACING_MODIFIER,
            spacingInPixels / - VERTICAL_SPACING_MODIFIER
        )
    }

    private fun createBottomSheetDialog(spot: Spot, larkingLotName: String, levelName: String) {
        val bottomSheetDialog = SpotBottomSheet(requireContext(), spot, UserRole.REGULAR.role)
        bottomSheetDialog.setUserSheetButtonListener {
            viewModel.takeUpSpot(spot.spotName, spot.spotType.toString(), larkingLotName, levelName) { responseMessage: String ->
                requireContext().showToast(responseMessage)
            }
        }
        bottomSheetDialog.getDialog().show()
    }

    private fun setObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        viewModel.serverErrorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
    }

    companion object {
        const val VERTICAL_SPACING_MODIFIER = 12
        const val HORIZONTAL_SPACING_MODIFIER = 2
    }
}
