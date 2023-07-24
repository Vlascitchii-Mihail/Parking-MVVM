package com.endava.parking.ui.createparkinglot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import androidx.core.view.isEmpty
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLevel
import com.endava.parking.databinding.LevelLayoutBinding
import com.endava.parking.databinding.LevelsViewBinding
import com.google.android.material.textfield.TextInputEditText

class LevelsView(private val context: Context, attr: AttributeSet? = null) :
    LinearLayout(context, attr) {

    //required foe getting container argument from the fragment
    private val binding = LevelsViewBinding.inflate(LayoutInflater.from(context), this, true)
    private val levelsMap: MutableMap<Int, String> = getInitialLevelsName()

    /**
     * @param container LevelsView instance in create_parking_layout.xml
     * @param checkConfirmBtnState check all fields for fullness and if all the fields are populated activates confirm button
     * @param onMaxLevelReached deactivates the addLevelButton if there is a maximum 5 of levels and activates if less than 5
     */
    fun addLevel(
        checkConfirmBtnState: () -> Unit,
        onMaxLevelReached: (Boolean) -> Unit
    ) {
        val newLevelBinding =
            LevelLayoutBinding.inflate(LayoutInflater.from(context), this, false)

        //get level with a sorted index
        val requiredLevel = getSortedLevelCredentials(this@LevelsView.children.toList())
        newLevelBinding.tvParkingLevel.text = requiredLevel.values.first()

        //I tried to use this@LevelsView and binding.llParkingLevelsContainer, but works only the argument from the fragment
        this@LevelsView.addView(newLevelBinding.root, requiredLevel.keys.first())

        newLevelBinding.inputSpot.addTextChangedListener { checkConfirmBtnState.invoke() }

        if (this@LevelsView.size == FragmentCreateParkingLot.MAX_LEVELS)
            onMaxLevelReached(true)

        checkConfirmBtnState.invoke()
        newLevelBinding.imgAdminDeleteLevelIcon.setOnClickListener {
            deleteLevel(this@LevelsView, newLevelBinding, onMaxLevelReached, checkConfirmBtnState)
        }
    }

    /**
     * @return sorted levels' names
     */
    private fun getSortedLevelCredentials(containerViewList: List<View>): Map<Int, String> {
        var sortedLevelCredentials = mutableMapOf<Int, String>()
        val levelsNameList = getLevelsNames(containerViewList)

        run forEach@{
            levelsMap.values.forEachIndexed { index, string ->
                if (!levelsNameList.contains(string)) {
                    sortedLevelCredentials = mutableMapOf(index + 1 to string)
                    return@forEach
                }
            }
        }
        return sortedLevelCredentials
    }

    /**
     * @return levels' name list from root view
     */
    private fun getLevelsNames(containerViewList: List<View>): List<String> {
        val nameList = mutableListOf<String>()
        containerViewList.forEach { view ->
            //was added same view dynamically, so I can't use binding, because views have same id
            nameList.add(view.findViewById<AppCompatTextView>(R.id.tv_parking_level).text.toString())
        }
        return nameList
    }

    /**
     * @return map key - index, value - level name
     */
    private fun getInitialLevelsName(): MutableMap<Int, String> {
        val levels: MutableMap<Int, String> = mutableMapOf()
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
        container: LevelsView,
        newLevelBinding: LevelLayoutBinding,
        onMaxLevelReached: (Boolean) -> Unit,
        checkConfirmBtnState: () -> Unit
    ) {
        container.removeView(newLevelBinding.root)
        if (container.size == FragmentCreateParkingLot.PENULTIMATE_LEVEL) onMaxLevelReached(false)
        checkConfirmBtnState.invoke()
    }

    /**
     * @return list of created ParkingLevel's objects
     */
    fun getParkingLevels(llParkingLevelsContainer: LevelsView): MutableList<ParkingLevel> {
        val levelsCapacity = mutableListOf<ParkingLevel>()
        llParkingLevelsContainer.children.forEach { view ->
            //was added same view dynamically, so I can't use binding, because views have same id
            val capacity = view.findViewById<TextInputEditText>(R.id.input_spot).text.toString().toInt()
            val level = view.findViewById<TextView>(R.id.tv_parking_level).text.toString()
            levelsCapacity.add(ParkingLevel(level, capacity))
        }
        return levelsCapacity
    }

    fun checkLevelsInputFullness(llParkingLevelsContainer: LevelsView) = with(llParkingLevelsContainer) {
        if (isEmpty()) true
        else {
            checkFieldsFullness(children)
        }
    }

    /**
     * check if all the fields are populated, it is required for activating the confirm button
     */
    private fun checkFieldsFullness(viewList: Sequence<View>): Boolean {
        val stateList: MutableList<Boolean> = mutableListOf()
        viewList.forEach { view ->
            stateList.add(
                //was added same view dynamically, so I can't use binding, because views have same id
                view.findViewById<TextInputEditText>(R.id.input_spot).text.toString().isNotEmpty()
            )
        }
        return !stateList.contains(false)
    }

    /**
     * add text Changed Listener to the first level A, it is best than adding listener each time when we add a level
     */
    fun setFirstLevelListener(checkConfirmBtnState: () -> Unit) {
        //I tried to use binding here, but it doesn't work in this case
        binding.inputSpot.addTextChangedListener { checkConfirmBtnState.invoke() }
    }
}
