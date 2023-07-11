package com.endava.parking.ui.parkinglots

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.User
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.FragmentParkingLotsBinding
import com.endava.parking.ui.utils.showLongToast
import com.endava.parking.ui.utils.showToast
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParkingLotsFragment : BaseFragment<FragmentParkingLotsBinding>(FragmentParkingLotsBinding::inflate) {

    private val searchBarAnimator = SearchBarAnimator()
    private val viewModel: ParkingLotsViewModel by viewModels()
    private val adapter by lazy { ParkingLotsAdapter(user.userRole, adapterClickListener) }
    private lateinit var qrScanIntegrator: IntentIntegrator
    private lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.getUserRole()
        setupFab()
        setupScanner()
        setupObservers()
        fetchParkingList()
        setupToolbarNavigation()
    }

    private fun fetchParkingList() { viewModel.fetchParkingLots(user) }

    private fun setupView() {
        user = checkNotNull(arguments?.getParcelable(USER_KEY))
        with (binding) {
            ivToolbarSearch.setOnClickListener { searchBarAnimator.showSearchToolbar(binding) }
            inputSearchParking.addTextChangedListener { viewModel.searchParking(it.toString()) }
            toolbarSearch.setNavigationOnClickListener {
                view?.let { activity?.hideKeyboard(it) }
                searchBarAnimator.hideSearchToolbar(binding)
            }
            swipeRefresh.setOnRefreshListener { fetchParkingList() }
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
            if (user.userRole == UserRole.ADMIN) {
                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fab_plus))
                fab.setOnClickListener {
                    requireContext().showLongToast("Create Parking !") // TODO replace with navigation to Create Parking
                }
            }
            if (user.userRole == UserRole.REGULAR) {
                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fab_qr))
                fab.setOnClickListener { performQrScanAction() }
            }
            fab.visibility = View.VISIBLE
        }
    }

    private fun setupObservers() = with(viewModel) {
        openSpotByQrCode.observe(viewLifecycleOwner) { requireContext().showToast(it) } // TODO - remove toast. Only for test
        progressBarVisibility.observe(viewLifecycleOwner) { binding.swipeRefresh.isRefreshing = it }
        fetchParkingLots.observe(viewLifecycleOwner) {
            user.userRole = UserRole.ADMIN    // TODO user status (admin/regular) must be extracted here !!!
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
    }

    private fun performQrScanAction() { qrScanIntegrator.initiateScan() }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.contents?.let { viewModel.openSpotByQrCode(result.contents) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private val adapterClickListener = { parkingId: String ->
        val bundle = Bundle()
        bundle.putString("parkingId", parkingId)
        if (user.userRole == UserRole.ADMIN) {
            // TODO - replace with navigation to Admin Details
            requireContext().showLongToast("User choose Parking Id - $parkingId, User - ${user.userRole}")
        } else {
            // TODO - replace with navigation to Regular Details
            requireContext().showLongToast("User choose Parking Id - $parkingId, User - ${user.userRole}")
        }
    }

    companion object { const val USER_KEY = "user" }
}
