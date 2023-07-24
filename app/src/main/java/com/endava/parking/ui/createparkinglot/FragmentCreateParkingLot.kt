package com.endava.parking.ui.createparkinglot

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.endava.parking.BaseFragment
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.databinding.FragmentCreateParkingBinding
import com.endava.parking.ui.utils.InputState
import com.endava.parking.ui.utils.InputTextType
import com.endava.parking.ui.utils.showToast
import com.endava.parking.ui.utils.createAlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCreateParkingLot : BaseFragment<FragmentCreateParkingBinding>(FragmentCreateParkingBinding::inflate) {

    private val viewModel: CreateParkingLotViewModel by viewModels()
    private val args: FragmentCreateParkingLotArgs  by navArgs()
    //initially, we don't have any level's view and we can't update level's view from XML
    private lateinit var timePickerHandler: TimePickerHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupNavigation()
        setupObservers()
    }

    private fun setupView() = with(binding.createParkingContainer) {
        timePickerHandler = TimePickerHandler(this@FragmentCreateParkingLot)

        //disable the keyboard for this inputEditText
        inputParkingOpHours.showSoftInputOnFocus = false
        timePickerHandler.setTimePickerListener(
            { inputParkingOpHours.clearFocus() },
            {
                viewModel.startTime = it[REQUEST_START_TIME]
                viewModel.endTime = it[REQUEST_END_TIME]
                inputParkingOpHours.setText(
                    getString(
                        R.string.lot_admin_operating_hours,
                        it[REQUEST_START_TIME],
                        it[REQUEST_END_TIME]
                    )
                )
            }
        )

        inputParkingOpHours.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) timePickerHandler.showTimePicker()
        }

        llParkingLevelsContainer.setFirstLevelListener { checkConfirmBtnState() }
        tvParkingAddLevel.setOnClickListener {
            llParkingLevelsContainer.addLevel(
                checkConfirmBtnState = { checkConfirmBtnState() },
                onMaxLevelReached = { isEnabled ->
                    binding.createParkingContainer.tvParkingAddLevel.isEnabled = !isEnabled }
            )


            scrollToBottom()
        }

        setTextChangedListener(inputParkingName)
        setTextChangedListener(inputParkingAddress)
        setTextChangedListener(inputParkingOpHours)

        chbParkingIsNonStop.setOnCheckedChangeListener { _, isChecked ->
            parkingWorkingDays.setDaysCheckBoxesAvailability(isChecked)

            if (isChecked) {
                //reset time in the inputParkingOpHours
                inputParkingOpHours.setText(
                    getString(
                        R.string.lot_admin_operating_hours,
                        INIT_START_TIME,
                        INIT_END_TIME
                    )
                )
                inputParkingOpHoursLayout.error = null
                viewModel.startTime = INIT_START_TIME
                viewModel.endTime = INIT_END_TIME
                inputParkingOpHours.isEnabled = false

            } else {
                inputParkingOpHours.isEnabled = true
                inputParkingOpHours.setText("")
            }
            checkConfirmBtnState()
        }

        parkingWorkingDays.setCheckBoxListeners{ checkConfirmBtnState() }

        binding.btnLotAdminConfirm.setOnClickListener {
            viewModel.validateInput(
                inputParkingName.text.toString(),
                inputParkingAddress.text.toString(),
                llParkingLevelsContainer.getParkingLevels(llParkingLevelsContainer),
                chbParkingIsNonStop.isChecked,
                chbParkingTempClosed.isChecked,
                parkingWorkingDays.getCheckedDaysString(),
                timePickerHandler.getValidationTimeStatus()
            )
        }
    }

    private fun setupNavigation() {
        binding.toolbarLotAdmin.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun scrollToBottom() {
        with(binding.scrollView) {
            post {
                fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }

    /**
     * activate confirm button if all the fields are populated
     */
    private fun checkConfirmBtnState() {
        viewModel.updateButtonState(
            checkViewsFullness()
        )
    }

    private fun setTextChangedListener(textInputEditTextItem: TextInputEditText) {
        textInputEditTextItem.addTextChangedListener {
            checkConfirmBtnState()
        }
    }

    //return true if all the fields are populated
    private fun checkViewsFullness(): Boolean {
        with(binding.createParkingContainer) {
            return checkInputFieldsFullness()
                && parkingWorkingDays.checkCheckboxesTicked(chbParkingIsNonStop.isChecked)
                && inputParkingOpHours.text.toString().isNotEmpty()
                && llParkingLevelsContainer.checkLevelsInputFullness(llParkingLevelsContainer)
        }
    }

    private fun checkInputFieldsFullness() = with(binding.createParkingContainer) {
        !inputParkingName.text.isNullOrEmpty() && !inputParkingAddress.text.isNullOrEmpty()
    }

    private fun setupObservers() {
        viewModel.buttonEnabled.observe(viewLifecycleOwner) { isPopulatedFields ->
            binding.btnLotAdminConfirm.isEnabled = isPopulatedFields
        }
        viewModel.validationStates.observe(viewLifecycleOwner) { stateList -> setErrorMessage(stateList) }
        viewModel.errorMessage.observe(viewLifecycleOwner) { requireContext().showToast(it) }
        //todo show dialog
        viewModel.navigateBack.observe(viewLifecycleOwner) {
            createAlertDialog(requireContext(), it) {
                val action =
                    FragmentCreateParkingLotDirections.actionFragmentCreateParkingLotToMockFragment(
                        ParkingLot(name = binding.createParkingContainer.inputParkingName.text.toString())
                    )
                findNavController().navigate(action)
            }?.show()
        }
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

    companion object {
        const val MAX_LEVELS = 5
        const val PENULTIMATE_LEVEL = 4
        const val REQUEST_START_TIME = "START TIME"
        const val REQUEST_END_TIME = "END TIME"
        const val INIT_START_TIME = "00:00"
        const val INIT_END_TIME = "00:00"
        const val POSITIVE_CODE = "201"
    }
}
