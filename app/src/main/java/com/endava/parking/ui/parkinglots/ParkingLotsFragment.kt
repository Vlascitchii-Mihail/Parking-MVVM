package com.endava.parking.ui.parkinglots

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.QrNavigation
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.FragmentParkingLotsBinding
import com.endava.parking.ui.utils.showToast
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParkingLotsFragment : BaseFragment<FragmentParkingLotsBinding>(FragmentParkingLotsBinding::inflate) {

    private val args: ParkingLotsFragmentArgs by navArgs()
    private val viewModel: ParkingLotsViewModel by viewModels()
    private val adapter by lazy { ParkingLotsAdapter(userRole, adapterClickListener) }
    private lateinit var qrScanIntegrator: IntentIntegrator
    private var userRole = UserRole.INVALID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRole = args.userrole

        setupView()
        setupObservers()
        setupScanner()
        setupFab()
        setupToolbarNavigation()
        fetchParkingList()
    }

    private fun fetchParkingList() { viewModel.fetchParkingLots() }

    private fun setupView() {
        with (binding) {
            ivToolbarSearch.setOnClickListener { showSearchToolbar() }
            inputSearchParking.addTextChangedListener { viewModel.searchParking(it.toString()) }
            toolbarSearch.setNavigationOnClickListener { hideSearchToolbar() }
            swipeRefresh.setOnRefreshListener { fetchParkingList() }
            swipeRefresh.setColorSchemeColors(resources.getColor(R.color.pomegranate))
            val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
            dividerItemDecoration.setDrawable(checkNotNull(ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable)))
            val recyclerView = rvParkingLots
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            toolbarSearchIcon.setOnClickListener { requireContext().showToast("Search action !") } // TODO - remove toast. Only for test
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
    }

    private fun setupFab() {
        with(binding) {
            if (userRole == UserRole.ADMIN) {
                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fab_plus))
                fab.setOnClickListener {
                    requireContext().showLongToast("Create Parking !") // TODO replace with navigation to Create Parking
                }
            }
            if (userRole == UserRole.REGULAR) {
                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fab_qr))
                fab.setOnClickListener { performQrScanAction() }
            }
            fab.isVisible = true
        }
    }
    private fun setupObservers() = with(viewModel) {
        openSpotByQrCode.observe(viewLifecycleOwner) { navigateToDetails(it) }
        progressBarVisibility.observe(viewLifecycleOwner) { binding.swipeRefresh.isRefreshing = it }
        serverErrorMessage.observe(viewLifecycleOwner) { requireContext().showLongToast(it) }
        fetchParkingLots.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener {
//            navigationCallback.popBackStack()   // TODO replace with Settings Screen
            requireContext().showToast("Settings action !") // TODO - remove toast. Only for test
        }
    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator.forSupportFragment(this)
        qrScanIntegrator.setPrompt(resources.getString(R.string.parking_lot_qr_prompt))
    }

    private fun performQrScanAction() { qrScanIntegrator.initiateScan() }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.contents?.let { viewModel.openSpotByQrCode(result.contents) }
    }

    private val adapterClickListener = { parkingLot: ParkingLot ->
        val action = ParkingLotsFragmentDirections.actionParkingLotsFragmentToTabsFragment(userRole)
        findNavController().navigate(action)
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun navigateToDetails(qrNavigation: QrNavigation) {
        /** Insert navigation code to Parking Spot */
        AlertDialog.Builder(requireContext())
            .setTitle("UNDER CONSTRUCTION !")
            .setMessage("Navigation to Parking - ${qrNavigation.parkingLot}, ${qrNavigation.level}, Spot - ${qrNavigation.parkingSpot}")
            .setPositiveButton("Okay !") { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}
