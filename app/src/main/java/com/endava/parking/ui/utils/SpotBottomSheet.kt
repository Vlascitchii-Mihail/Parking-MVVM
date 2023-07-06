package com.endava.parking.ui.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.endava.parking.R
import com.endava.parking.data.model.Spot
import com.endava.parking.data.model.SpotType
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.BottomUserSheetBinding
import com.endava.parking.ui.spotslist.SpotsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class SpotBottomSheet(
    private val context: Context,
    private val spot: Spot,
    private val uerRole: String? = UserRole.REGULAR.role
) {

    private val dialog = BottomSheetDialog(context)
    private lateinit var binding: ViewBinding

    init {
        //will inflate different views in the future
        when (uerRole) {
            UserRole.REGULAR.role -> {
                binding = BottomUserSheetBinding.inflate(LayoutInflater.from(context))
                createUserParkingViewSheet()
                dialog.setContentView((binding as BottomUserSheetBinding).root)
            }
            else -> {}
        }
    }

    fun getDialog() = dialog

    private fun createUserParkingViewSheet() {
        with(binding as BottomUserSheetBinding) {
            tvSheetTypeField.text = getSpotsTypeName(spot.spotType)
            tvSheetSpotName.text = spot.spotName
            imgSheetType.setImageResource(SpotsAdapter.ParkingLotViewHolder.getSpotImageId(spot.spotType))
        }
    }

    private fun getSpotsTypeName(spotType: SpotType): String {
        return when (spotType) {
            SpotType.FAMILY -> context.getString(R.string.sheet_park_spot_family)
            SpotType.DISABLED_PERSON -> context.getString(R.string.sheet_park_spot_disable)
            SpotType.REGULAR -> context.getString(R.string.sheet_park_spot_regular)
            SpotType.TEMPORARY_CLOSED -> { "" }
        }
    }

    fun setUserSheetButtonListener(takeUpSpot: () -> Unit) {
        (binding as BottomUserSheetBinding).btnSheetPark.setOnClickListener {
            takeUpSpot.invoke()
        }
    }
}
