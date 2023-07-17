package com.endava.parking.ui.createparkinglot

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.databinding.FragmentCreateParkingBinding
import com.endava.parking.ui.tabsparkinglot.TabPagerAdapter
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.InputTextType
import com.endava.parking.ui.utils.showToast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCreateParkingLot : BaseFragment<FragmentCreateParkingBinding>(FragmentCreateParkingBinding::inflate) {

    private val viewModel: CreateParkingLotViewModel by viewModels()
    //initially, we don't have any level's view and we can't update level's view from XML
    private lateinit var levelHandler: LevelsHandler
    private lateinit var timePickerHandler: TimePickerHandler
    ///TODO TO VIEWMODEL
    private var actionType: String? = TabPagerAdapter.EDIT_ACTION

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        actionType = arguments?.getString(ACTION_TYPE)
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        arguments?.getString(ACTION_TYPE)?.let {
//            actionType = it }

//        actionType = arguments?.getString(ACTION_TYPE)

//        actionType = arguments?.getString(ACTION_TYPE)
        setupView()
        setupObservers()
    }

    private fun mockNavSafeArgsGetUserRoleAndLotId() = listOf("1", "Admin")

    private fun setupView() = with(binding.createParkingContainer) {
        levelHandler = LevelsHandler(requireContext())
        timePickerHandler = TimePickerHandler(requireContext())

        if (arguments?.getString(ACTION_TYPE) == TabPagerAdapter.EDIT_ACTION) viewModel.getParkingLot(mockNavSafeArgsGetUserRoleAndLotId().first())

        //disable the keyboard for this inputEditText
        inputParkingOpHours.showSoftInputOnFocus
        inputParkingOpHours.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) timePickerHandler.getStartPicker().show(childFragmentManager, REQUEST_START_TIME)
        }

        tvParkingAddLevel.setOnClickListener {
            levelHandler.addLevel(
                //LinearLayout container
                this.llParkingLevelsContainer,
//                binding.createParkingContainer.root,
                { setConfirmBtnAvailabilityState() }) { isEnabled ->
                binding.createParkingContainer.tvParkingAddLevel.isEnabled = isEnabled
            }

            scrollToBottom(binding.root)
        }

        setTextChangedListener(inputParkingName)
        setTextChangedListener(inputParkingAddress)
        setTextChangedListener(inputParkingSpotA)

        chbParkingIsNonStop.setOnCheckedChangeListener { _, isChecked ->
            parkingWorkingDays.setDaysCheckBoxesAvailability(isChecked)

            if (isChecked) {
                //reset time in the inputParkingOpHours
                inputParkingOpHours.setText(
                    context?.getString(
                        R.string.lot_admin_operating_hours,
                        START_TIME,
                        END_TIME
                    )
                )
                inputParkingOpHoursLayout.error = null
                viewModel.startTime = emptyList()
                viewModel.endTime = emptyList()
                inputParkingOpHours.isEnabled = false

            } else {
                inputParkingOpHours.isEnabled = true
                inputParkingOpHours.setText("")
            }
            setConfirmBtnAvailabilityState()
        }

        parkingWorkingDays.setCheckBoxListeners{ setConfirmBtnAvailabilityState() }

        timePickerHandler.setTimePickerListener(
            //show endTimePicker
            { timePickerHandler.getEndPicker().show(parentFragmentManager, END_TIME) },
            //save time to the viewModel
            { timeMap -> if (timeMap.keys.first() == REQUEST_START_TIME) {
                viewModel.startTime = timeMap.values.first() } else viewModel.endTime = timeMap.values.first() },
            { setConfirmBtnAvailabilityState() },
            { inputParkingOpHours.clearFocus() }
        ) { time: String -> inputParkingOpHours.setText(time) }

        binding.btnLotAdminConfirm.setOnClickListener {
            viewModel.validateInput(
                inputParkingName.text.toString(),
                inputParkingAddress.text.toString(),
                inputParkingSpotA.text.toString(),
                levelHandler.getParkingLevels(llParkingLevelsContainer.children, inputParkingSpotA.text.toString()),
                chbParkingIsNonStop.isChecked,
                chbParkingTempClosed.isChecked,
                parkingWorkingDays.getCheckedDaysStringList()
            )
        }
    }

    private fun scrollToBottom(scrollView: ScrollView) {
        with(scrollView) {
            post {
                fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }

    private fun setConfirmBtnAvailabilityState() {
        viewModel.checkButtonState(
            checkViewsFullness()
        )
    }

    private fun setTextChangedListener(textInputEditTextItem: TextInputEditText) {
        textInputEditTextItem.addTextChangedListener {
            setConfirmBtnAvailabilityState()
        }
    }

    //return true if all the fields are populated
    private fun checkViewsFullness(): Boolean {
        with(binding.createParkingContainer) {
            return checkInputFieldsFullness()
                && parkingWorkingDays.checkCheckboxesTicked(chbParkingIsNonStop.isChecked)
                && inputParkingOpHours.text.toString().isNotEmpty()
                && levelHandler.checkLevelsInputFullness(llParkingLevelsContainer)
        }
    }

    private fun checkInputFieldsFullness() = with(binding.createParkingContainer) {
        !inputParkingName.text.isNullOrEmpty() && !inputParkingAddress.text.isNullOrEmpty()
            && !inputParkingSpotA.text.isNullOrEmpty()
    }

    private fun setupObservers() {
        viewModel.buttonEnabled.observe(viewLifecycleOwner) { isPopulatedFields ->
            binding.btnLotAdminConfirm.isEnabled = isPopulatedFields
        }
        viewModel.validationStates.observe(viewLifecycleOwner) { stateList -> setErrorMessage(stateList) }
        viewModel.errorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        viewModel.serverErrorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        viewModel.parkingLotInstance.observe(viewLifecycleOwner) { initializeFields(it) }
    }

    private fun setErrorMessage(stateList: List<InputState>) {
        with(binding.createParkingContainer) {

            stateList.forEach { state ->
                when (state.fieldType) {
                    InputTextType.NAME -> {
                        inputParkingNameLayout.error = if (state.isValid) null
                        else requireContext().getString(state.errorMessage)
                    }
                    InputTextType.ADDRESS -> {
                        inputParkingAddressLayout.error = if (state.isValid) null
                        else requireContext().getString((state.errorMessage))
                    }
                    InputTextType.SPOTS -> {
                        inputParkingSpotALayout.error = if(state.isValid) null
                        else requireContext().getString(state.errorMessage)
                    }
                    InputTextType.TIME -> {
                        inputParkingOpHoursLayout.error = if(state.isValid) null
                        else requireContext().getString(state.errorMessage)
                    }
                    else -> {}
                }
            }

            llParkingLevelsContainer.children.forEachIndexed { index, value ->
                value.findViewById<TextInputLayout>(R.id.input_spot_layout).error =
                    if (viewModel.levelsErrorIndexes.isNotEmpty() && viewModel.levelsErrorIndexes.contains(index+1))
                        requireContext().getString(R.string.lot_admin_error_levels)
                    else null
            }
        }
    }

    private fun initializeFields(parkingLot: ParkingLot) = with(binding.createParkingContainer) {
        binding.toolbarLotAdmin.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.title = parkingLot.name

        //set custom margins to he highest view
        val param = inputParkingName.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(MARGIN_ZERO, R.dimen.margin_large, MARGIN_ZERO, MARGIN_ZERO)
        binding.createParkingContainer.inputParkingName.layoutParams = param

        inputParkingName.setText(parkingLot.name)
        inputParkingAddress.setText(parkingLot.address)
        if (parkingLot.startHour != null && parkingLot.endHour != null) inputParkingOpHours.setText(
            requireContext().getString(
                R.string.lot_admin_operating_hours,
                parkingLot.startHour,
                parkingLot.endHour
            )
        )
        parkingLot.days?.let { parkingWorkingDays.setCheckedDays(it) }
        parkingLot.isNonStop?.let { chbParkingIsNonStop.isChecked = it }
        parkingLot.isClosed?.let { chbParkingTempClosed.isChecked = it }

        parkingLot.levels.forEach { level ->
            if (level.name == INITIAL_MANDATORY_LEVEL) {
                binding.createParkingContainer.tvParkingLevelA.text = level.name
                binding.createParkingContainer.inputParkingSpotA.setText(level.capacity.toString())
            }
            else levelHandler.addLevel(llParkingLevelsContainer, { setConfirmBtnAvailabilityState() }) { isEnabled ->
                binding.createParkingContainer.tvParkingAddLevel.isEnabled = isEnabled
            }
        }
    }

    companion object {
//        fun newInstance(actionType: String): FragmentCreateParkingLot {
//            val createParkingLot = FragmentCreateParkingLot()
//            val bundle = Bundle()
//            bundle.putString(ACTION_TYPE, actionType)
//            createParkingLot.arguments = bundle
//            return createParkingLot
//        }
//        fun newInstance(actionType: String) = FragmentCreateParkingLot().apply {
//            arguments = bundleOf(ACTION_TYPE to actionType)
//        }
        fun newInstance(actionType: String) = FragmentCreateParkingLot().apply {
            arguments = bundleOf(ACTION_TYPE to actionType)
        }
        const val MAX_LEVELS = 4
        const val PENULTIMATE_LEVEL = 3
        const val REQUEST_START_TIME = "START TIME"
        const val REQUEST_END_TIME = "END TIME"
        const val START_TIME = "00:00"
        const val END_TIME = "00:00"
        const val MARGIN_ZERO = 0
        const val INITIAL_MANDATORY_LEVEL = "Level A"
        const val ACTION_TYPE = "ACTION TYPE"
    }
}
