package com.endava.parking.ui.spotslist

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
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
    private lateinit var spotDetails: SpotDetails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spotDetails = checkNotNull(arguments?.getParcelable(SPOTS_KEY))
//        viewModel.getParkingSpots()

        setupView()
    }

    private fun setupView() {
        setupRecyclerView()
        setObservers()
//        viewModel.parkingLotInstance.value?.let { spotsList ->
//            setupSpinnerAppearance(spotDetails.parkingLevel.map { it.name })
////            setupSpinner(spotsList)
//        }
        setupSpinnerAppearance(spotDetails.parkingLevel.map { it.name })

    }

    private fun setupSpinnerAppearance(levels: List<String>) = with(binding) {
        spnLevels.visibility = if (levels.size == 1)  {
//            spotsListAdapter.submitList(levels.first().spots)
//            spotsListAdapter.submitList(viewModel.getParkingSpots(spotDetails.parkingName, spotDetails.parkingLevel.first().name))
            viewModel.getParkingSpots(spotDetails.parkingName, spotDetails.parkingLevel.first().name)
            View.GONE
        }
        else {
            setupSpinner(levels)
            View.VISIBLE
        }
    }

    private fun setupSpinner(levels: List<String>) = with(binding){
//        val levelsMap = getLevelsName(levels)
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            levels
        )
        spnLevels.adapter = spinnerAdapter
        setupSpinnerAction()
    }

    private fun setupSpinnerAction() = with(binding) {
        spnLevels.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getParkingSpots(spotDetails.parkingName, spotDetails.parkingLevel[position].name)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

//    private fun getLevelsName(levels: List<ParkingLevel>): MutableMap<String, List<Spot>> {
//        val levelsName = mutableMapOf<String, List<Spot>>()
//        levels.forEach { level ->
////            levelsName[level.name] = level.spots
//        }
//        return levelsName
//    }

    private fun setupRecyclerView() = with(binding){
        rvTabSpotsList.layoutManager = GridLayoutManager(requireContext(), 3)
        spotsListAdapter = SpotsAdapter { spot: Spot ->
//            createBottomSheetDialog(spot, viewModel.lotDescription.first(), viewModel.lotDescription.last())
            //first ??????
            createBottomSheetDialog(spot, spotDetails.parkingName, spotDetails.parkingLevel.first().name)
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

    private fun createBottomSheetDialog(spot: Spot, parkingLotName: String, levelName: String) {
        val bottomSheetDialog = SpotBottomSheet(requireContext(), spot, UserRole.REGULAR.role)
        bottomSheetDialog.setUserSheetButtonListener {
            viewModel.takeUpSpot(spot.spotName, spot.spotType.toString(), parkingLotName, levelName) { responseMessage: String ->
                requireContext().showToast(responseMessage)
            }
        }
        bottomSheetDialog.getDialog().show()
    }

    private fun setObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        viewModel.serverErrorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        viewModel.parkingSpots.observe(viewLifecycleOwner) { spotsListAdapter.submitList(it) }
    }

    companion object {
        const val VERTICAL_SPACING_MODIFIER = 12
        const val HORIZONTAL_SPACING_MODIFIER = 2
        const val SPOTS_KEY = "spots_key"

        fun newInstance(spotDetails: SpotDetails) = SpotsFragment().apply {
            arguments = bundleOf(SPOTS_KEY to spotDetails)
        }
    }
}
