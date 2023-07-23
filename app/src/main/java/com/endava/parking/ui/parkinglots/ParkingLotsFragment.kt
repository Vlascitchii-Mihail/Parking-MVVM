package com.endava.parking.ui.parkinglots

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.QrNavigation
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.FragmentParkingLotsBinding
import com.endava.parking.ui.tabs.IntentType
import com.endava.parking.ui.tabs.NavigationIntent
import com.endava.parking.ui.utils.showLongToast
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
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        userRole = args.userrole

        setupView()
        setupFab()
        setupScanner()
        setupObservers()
        fetchParkingList()
        setupToolbarNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun fetchParkingList() { viewModel.fetchParkingLots() }

    private val openKeyboardListener = { binding.fab.isVisible = false }
    private val closeKeyboardListener = { binding.fab.isVisible = true }

    private fun setupView() {
        with (binding) {
            setOnKeyboardStateListener(openKeyboardListener, closeKeyboardListener)
            ivToolbarSearch.setOnClickListener { showSearchBar() }
            inputSearchParking.addTextChangedListener { viewModel.searchParking(it.toString()) }
            toolbarSearch.setNavigationOnClickListener { hideSearchBar() }
            swipeRefresh.setOnRefreshListener { fetchParkingList() }
            swipeRefresh.setColorSchemeColors(resources.getColor(R.color.pomegranate))
            val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
            dividerItemDecoration.setDrawable(checkNotNull(ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable)))
            val recyclerView = rvParkingLots
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            inputSearchParking.setText("")
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
    }

    private fun showSearchBar() {
        with (binding) {
            swipeRefresh.setOnRefreshListener { swipeRefresh.isRefreshing = false }
            inputSearchParking.setText("")
            toolbar.isVisible = false
            toolbarSearch.isVisible = true
            inputSearchParking.requestFocus()
            fab.visibility = View.GONE
        }
        showKeyboard()
    }

    private fun hideSearchBar() {
        with (binding) {
            inputSearchParking.setText("")
            toolbarSearch.visibility = View.INVISIBLE
            toolbar.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            swipeRefresh.setOnRefreshListener { fetchParkingList() }
        }
        hideKeyboard()
    }

    private fun setupFab() {
        with(binding) {
            if (userRole == UserRole.ADMIN) {
                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fab_plus))
                fab.setOnClickListener {
                    findNavController().navigate(R.id.action_parkingLots_to_createParkingFragment)
//                    val action = ParkingLotsFragmentDirections.actionParkingLotsToTabsFragment(
//                        NavigationIntent(IntentType.ADMIN_TO_CREATE)
//                    )
//                    findNavController().navigate(action)
                }
            }
            if (userRole == UserRole.REGULAR) {
                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fab_qr))
                fab.setOnClickListener { performQrScanAction() }
            }
            fab.show()
        }
    }

    private fun setupObservers() = with(viewModel) {
        openSpotByQrCode.observe(viewLifecycleOwner) { navigateToDetails(it) }
        progressBarVisibility.observe(viewLifecycleOwner) { binding.swipeRefresh.isRefreshing = it }
        serverErrorMessage.observe(viewLifecycleOwner) { requireContext().showLongToast(it) }
        fetchParkingLots.observe(viewLifecycleOwner) {
            adapter.submitList(it)
//            adapter.setData(it)
            if (it != null) { binding.tvSearchUnsuccessful.isVisible = it.isEmpty() }
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
        /**
        QR code example -    {"parkingLot":"Endava","level":"Level A","parkingSpot":"A-012"}
        */
    }

    private fun performQrScanAction() { qrScanIntegrator.initiateScan() }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.contents?.let { viewModel.openSpotByQrCode(result.contents) }
    }

    private val adapterClickListener = { parkingLot: ParkingLot ->
        binding.inputSearchParking.setText("")
        val action = ParkingLotsFragmentDirections.actionParkingLotsToTabsFragment(
            NavigationIntent(
                if (userRole == UserRole.ADMIN) {
                    IntentType.ADMIN_TO_UPDATE
                } else {
                    IntentType.REGULAR_TO_DETAILS
                },
                parkingLot
            )
        )
        findNavController().navigate(action)
    }

    private fun navigateToDetails(qrNavigation: QrNavigation) {
        val action = ParkingLotsFragmentDirections.actionParkingLotsToTabsFragment(
            NavigationIntent(
                IntentType.REGULAR_TO_SPOTS,
                qrNavigation = qrNavigation
            )
        )
        findNavController().navigate(action)
//        /** Insert navigation code to Parking Spot */
//        AlertDialog.Builder(requireContext())
//            .setTitle("UNDER CONSTRUCTION !")
//            .setMessage("Navigation to Parking - ${qrNavigation.parkingLot}, ${qrNavigation.level}, Spot - ${qrNavigation.parkingSpot}")
//            .setPositiveButton("Okay !") { _, _ -> }
//            .setIcon(android.R.drawable.ic_dialog_alert)
//            .show()
    }
}
