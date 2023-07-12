package com.endava.parking.ui.parkinglots

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.QrNavigation
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.FragmentParkingLotsBinding
import com.endava.parking.ui.utils.hideKeyboard
import com.endava.parking.ui.utils.showLongToast
import com.endava.parking.ui.utils.showToast
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParkingLotsFragment : BaseFragment<FragmentParkingLotsBinding>(FragmentParkingLotsBinding::inflate) {

    private var userRole = UserRole.INVALID
    private val viewModel: ParkingLotsViewModel by viewModels()
    private val adapter by lazy { ParkingLotsAdapter(userRole, adapterClickListener) }
    private lateinit var qrScanIntegrator: IntentIntegrator
    private lateinit var token: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
        setupScanner()
        setupFab()
        setupToolbarNavigation()
        fetchParkingList()
    }

    private fun fetchParkingList() { viewModel.fetchParkingLots(token) }

    private fun setupView() {
        val credentials: ParkingLotsCredentials? = arguments?.getParcelable(CREDENTIALS)
        token = checkNotNull(credentials?.token)
        userRole = checkNotNull(credentials?.userRole)
        with (binding) {
            inputSearchParking.addTextChangedListener { viewModel.searchParking(it.toString()) }
            /** Show Search Toolbar */
            ivToolbarSearch.setOnClickListener {
                toolbarSearch.visibility = View.VISIBLE
                toolbar.visibility = View.INVISIBLE
                inputSearchParking.requestFocus()
                binding.fab.hide()
            }
            /** Hide Search Toolbar */
            toolbarSearch.setNavigationOnClickListener {
                view?.let { activity?.hideKeyboard(it) }
                inputSearchParking.setText("")
                toolbar.visibility = View.VISIBLE
                toolbarSearch.visibility = View.INVISIBLE
                fab.show()
            }
            swipeRefresh.setOnRefreshListener { fetchParkingList() }
            swipeRefresh.setColorSchemeColors(resources.getColor(R.color.pomegranate))
            val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
            dividerItemDecoration.setDrawable(checkNotNull(ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable)))
            val recyclerView = rvParkingLots
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
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
            fab.visibility = View.VISIBLE
        }
    }

    private fun setupObservers() = with(viewModel) {
        openSpotByQrCode.observe(viewLifecycleOwner) { navigateToDetails(it) }
        qrCodeError.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        progressBarVisibility.observe(viewLifecycleOwner) { binding.swipeRefresh.isRefreshing = it }
        setUserRole.observe(viewLifecycleOwner) {
            userRole = it
            setupFab()
            adapter.setUserRole(it)
            adapter.notifyDataSetChanged()
        }
        fetchParkingLots.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            setupFab()
            binding.tvSearchUnsuccessful.isVisible = it.isEmpty()
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
        // QR code example -    {"parkingLot":"Endava","level":"Level A","parkingSpot":"12"}
    }

    private fun performQrScanAction() { qrScanIntegrator.initiateScan() }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.contents?.let { viewModel.openSpotByQrCode(result.contents) }
    }

    private val adapterClickListener = { parkingId: String ->
        val bundle = Bundle()
        bundle.putString("parkingId", parkingId)
        if (userRole == UserRole.ADMIN) {
            // TODO - replace with navigation to Admin Details
            requireContext().showLongToast("User choose Parking Id - $parkingId, User - $userRole")
        }
        if (userRole == UserRole.REGULAR) {
            // TODO - replace with navigation to Regular Details
            requireContext().showLongToast("User choose Parking Id - $parkingId, User - $userRole")
        }
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

    companion object {
        const val USER_KEY = "user"
        const val CREDENTIALS = "credentials"
    }
}
