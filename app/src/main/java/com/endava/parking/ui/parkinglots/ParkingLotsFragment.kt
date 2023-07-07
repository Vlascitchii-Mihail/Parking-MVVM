package com.endava.parking.ui.parkinglots

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
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
import com.endava.parking.ui.utils.showToast
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

private const val USER = "user"
private const val DURATION: Long = 400L

@AndroidEntryPoint
class ParkingLotsFragment : BaseFragment<FragmentParkingLotsBinding>(FragmentParkingLotsBinding::inflate) {

    private val viewModel: ParkingLotsViewModel by viewModels()
    private val adapter by lazy { ParkingLotsAdapter(UserRole.REGULAR, adapterClickListener) }
    private lateinit var qrScanIntegrator: IntentIntegrator
    private lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
//        setupFab()
        setupScanner()
        setupObservers()
        fetchParkingList()
        setupToolbarNavigation()
    }

    private fun fetchParkingList() { viewModel.fetchParkingLots(user) }

    private fun setupView() {
        user = checkNotNull(arguments?.getParcelable(USER_KEY))
        with (binding) {
            ivToolbarSearch.setOnClickListener { showSearchToolbar() }
            inputSearchParking.addTextChangedListener { viewModel.searchParking(it.toString()) }
            toolbarSearch.setNavigationOnClickListener { hideSearchToolbar() }
            swipeRefresh.setOnRefreshListener { fetchParkingList() }
            val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
            dividerItemDecoration.setDrawable(checkNotNull(ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable)))
            val recyclerView = rvParkingLots
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            toolbarSearchIcon.setOnClickListener { requireContext().showToast("Search action !") } // TODO - remove toast. Only for test
        }
    }

//    private fun setupFab() {
//        with(binding) {
//            if (user.userRole == UserRole.ADMIN) {
//                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fab_plus))
//                fab.setOnClickListener {
//                    requireContext().showLongToast("Create Parking !") // TODO replace with navigation to Create Parking
//                }
//            }
//            if (user.userRole == UserRole.REGULAR) {
//                fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_fab_qr))
//                fab.setOnClickListener { performQrScanAction() }
//            }
//            fab.visibility = View.VISIBLE
//        }
//    }

    private fun setupObservers() = with(viewModel) {
        openSpotByQrCode.observe(viewLifecycleOwner) { requireContext().showToast(it) } // TODO - remove toast. Only for test
        progressBarVisibility.observe(viewLifecycleOwner) { binding.swipeRefresh.isRefreshing = it }
        fetchParkingLots.observe(viewLifecycleOwner) {
//            user.userRole = UserRole.REGULAR    // TODO user status (admin/regular) must be extracted here !!!
            adapter.submitList(it)
//            setupFab()
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

    private fun showSearchToolbar() {
        binding.toolbarSearch.visibility = View.VISIBLE
        binding.inputSearchParking.requestFocus()
        val searchPath = Path().apply {
            moveTo(1500f, 0f)
            lineTo(0f, 0f)
        }
        val toolbarPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(-1500f, 0f)
        }
        val searchAnimator = ObjectAnimator.ofFloat(binding.toolbarSearch, View.X, View.Y, searchPath).apply {
            duration = 400
            start()
        }
        val toolbarAnimator = ObjectAnimator.ofFloat(binding.toolbar, View.X, View.Y, toolbarPath).apply {
            duration = 400
            start()
        }
    }

    private fun hideSearchToolbar() {
        binding.inputSearchParking.setText("")
        view?.let { activity?.hideKeyboard(it) }
        val searchPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(-1500f, 0f)
        }
        val toolbarPath = Path().apply {
            moveTo(1500f, 0f)
            lineTo(0f, 0f)
        }
        val searchAnimator = ObjectAnimator.ofFloat(binding.toolbarSearch, View.X, View.Y, searchPath).apply {
            duration = DURATION
            start()
        }
        val toolbarAnimator = ObjectAnimator.ofFloat(binding.toolbar, View.X, View.Y, toolbarPath).apply {
            duration = DURATION
            start()
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private val adapterClickListener = { parkingId: String ->
        val bundle = Bundle()
        bundle.putString("parkingId", parkingId)
//        if (user.userRole == UserRole.ADMIN) {
//            // TODO - replace with navigation to Admin Details
//            requireContext().showLongToast("User choose Parking Id - $parkingId, Admin - ${user.userRole}")
//        } else {
//            // TODO - replace with navigation to Regular Details
//            requireContext().showLongToast("User choose Parking Id - $parkingId, Admin - ${user.userRole}")
//        }
    }

    companion object { const val USER_KEY = "user" }
}
