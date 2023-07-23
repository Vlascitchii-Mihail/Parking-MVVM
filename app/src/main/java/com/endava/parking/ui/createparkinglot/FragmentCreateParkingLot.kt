package com.endava.parking.ui.createparkinglot

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.databinding.FragmentCreateParkingBinding
import com.endava.parking.ui.details.DetailsFragment
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
    private var parking: ParkingLot? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parking = arguments?.getParcelable(PARKING_KEY)

        setupView()
        setupObservers()
        setupToolbarNavigation()
    }

    private fun setupView() = with(binding.createParkingContainer) {
        levelHandler = LevelsHandler(requireContext())
        timePickerHandler = TimePickerHandler(requireContext())

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

            scrollToBottom(binding.scrollView)
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

    private fun setupToolbarNavigation() {
        binding.toolbarLotAdmin.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val MAX_LEVELS = 4
        const val PENULTIMATE_LEVEL = 3
        const val REQUEST_START_TIME = "START TIME"
        const val REQUEST_END_TIME = "END TIME"
        const val START_TIME = "00:00"
        const val END_TIME = "00:00"

        private const val PARKING_KEY = "parking_key"
        fun newInstance(parkingLot: ParkingLot) = DetailsFragment().apply {
            arguments = bundleOf(PARKING_KEY to parkingLot)
        }
    }
}
