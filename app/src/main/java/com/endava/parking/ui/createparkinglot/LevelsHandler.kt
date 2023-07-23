package com.endava.parking.ui.createparkinglot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isEmpty
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLevelToRequest
import com.endava.parking.databinding.AddLevelLayoutBinding
import com.google.android.material.textfield.TextInputEditText

class LevelsHandler(private val context: Context, attr: AttributeSet? = null) :
    ConstraintLayout(context, attr) {

    private val levelsMap: MutableMap<Int, String> = getInitialLevelsMap()

    fun addLevel(
        layoutContainer: LinearLayout,
        setConfirmBtnAvailabilityState: () -> Unit,
        setupAddLevelOpportunity: (Boolean) -> Unit
    ) {

        val binding = AddLevelLayoutBinding.inflate(LayoutInflater.from(context), this, false)
        with(layoutContainer) {

            //get one required level with the correct index
            val requiredLevel = getSortedLevel(children.toList())
            binding.tvParkingLevel.text = requiredLevel.values.first()

            //add root view to container with special index
            addView(binding.root, requiredLevel.keys.first())

            binding.inputSpot.addTextChangedListener { setConfirmBtnAvailabilityState.invoke() }
            if (size == FragmentCreateParkingLot.MAX_LEVELS) setupAddLevelOpportunity(false)
        }

        setConfirmBtnAvailabilityState.invoke()
        binding.imgAdminDeleteLevelIcon.setOnClickListener {
            deleteLevel(binding, layoutContainer, setupAddLevelOpportunity, setConfirmBtnAvailabilityState)
        }

    }

    //return sorted level with index
    private fun getSortedLevel(containerViewList: List<View>): Map<Int, String> {
        var sortedOneLevelMap = mutableMapOf<Int, String>()
        val containerLevelsNameList = getLevelsNameList(containerViewList)

        run forEach@{
            levelsMap.values.forEachIndexed { index, string ->
                if (containerViewList.isNotEmpty() && !containerLevelsNameList.contains(string)) {
                    sortedOneLevelMap = mutableMapOf(index to string)
                    return@forEach
                } else if (containerViewList.isEmpty()) {
                    sortedOneLevelMap = mutableMapOf(0 to "Level B")
                    return@forEach
                }
            }
        }
        return sortedOneLevelMap
    }

    //return name list of levels name in LinearLayout container
    private fun getLevelsNameList(viewList: List<View>): List<String> {
        val stringList = mutableListOf<String>()
        viewList.forEach { view ->
            stringList.add(getDataFromNestedDynamicTextView(view, R.id.tv_parking_level))
        }
        return stringList
    }

    private fun getInitialLevelsMap(): MutableMap<Int, String> {
        val levels: MutableMap<Int, String> = mutableMapOf()
        with(context) {

        }
        val levelsList = with(context) {
            listOf(
                getString(R.string.lot_admin_level_b),
                getString(R.string.lot_admin_level_c),
                getString(R.string.lot_admin_level_d),
                getString(R.string.lot_admin_level_e)
            )
        }
        for (index in levelsList.indices) {
            levels[index] = levelsList[index]
        }
        return levels
    }

    private fun deleteLevel(
        binding: AddLevelLayoutBinding,
        layoutContainer: LinearLayout,
        setupAddLevelOpportunity: (Boolean) -> Unit,
        setConfirmBtnAvailabilityState: () -> Unit
    ) {
        with(layoutContainer) {
            removeView(binding.root)
            if (size == FragmentCreateParkingLot.PENULTIMATE_LEVEL) setupAddLevelOpportunity(true)
        }
        setConfirmBtnAvailabilityState.invoke()
    }

    fun getParkingLevels(
        childrenViews: Sequence<View>,
        lotLevelASpots: String
    ): MutableList<ParkingLevelToRequest> {
        val levelsCapacity =
            //add first mandatory level A
            mutableListOf(
                ParkingLevelToRequest(
                    context.getString(R.string.lot_admin_level_a),
                    lotLevelASpots.toInt()
                )
            )

        childrenViews.forEach { view ->
            val capacity = getDataFromNestedDynamicTextInput(view, R.id.input_spot).toInt()
            val level = getDataFromNestedDynamicTextView(view, R.id.tv_parking_level)
            levelsCapacity.add(ParkingLevelToRequest(level, capacity))
        }
        return levelsCapacity
    }

    fun checkLevelsInputFullness(levelsContainer: LinearLayout) = with(levelsContainer) {
        if (isEmpty()) true
        else {
            checkFieldsFullness(children)
        }
    }

    private fun checkFieldsFullness(viewList: Sequence<View>): Boolean {
        val stateList: MutableList<Boolean> = mutableListOf()
        viewList.forEach {
            stateList.add(
                getDataFromNestedDynamicTextInput(it, R.id.input_spot).isNotEmpty()
            )
        }
        return !stateList.contains(false)
    }

    private fun getDataFromNestedDynamicTextView(view: View, viewId: Int): String {
        return view.findViewById<AppCompatTextView>(viewId).text.toString()
    }

    private fun getDataFromNestedDynamicTextInput(view: View, viewId: Int): String {
        return view.findViewById<TextInputEditText>(viewId).text.toString()
    }
}
