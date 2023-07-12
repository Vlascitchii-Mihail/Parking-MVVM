package com.endava.parking.ui.parkinglots

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.endava.parking.R
import com.endava.parking.data.model.ParkingLot
import com.endava.parking.data.model.UserRole
import com.endava.parking.databinding.ItemParkingLotBinding
import com.endava.parking.ui.utils.colorWorkingDays

class ParkingLotsAdapter(
    private var userRole: UserRole,
    private val onListItemClickListener: (String) -> Unit,
) : ListAdapter<ParkingLot, ParkingLotsAdapter.ViewHolder>(ListItemCallback()) {

    class ListItemCallback : DiffUtil.ItemCallback<ParkingLot>() {
        override fun areItemsTheSame(oldItem: ParkingLot, newItem: ParkingLot): Boolean = (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: ParkingLot, newItem: ParkingLot): Boolean = (oldItem == newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParkingLotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    fun setUserRole(role: UserRole) { userRole = role }

    inner class ViewHolder(private val binding: ItemParkingLotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ParkingLot) {
            binding.apply {
                parkingLotArrowIcon.isVisible = !(item.isClosed == true && userRole == UserRole.REGULAR)
                if (item.isClosed == true && userRole == UserRole.REGULAR) root.setOnClickListener(null)
                else { root.setOnClickListener{ onListItemClickListener(item.id) } }

                /** Regular Mode */
                parkingLotName.text = item.name
                parkingLotOpenHours.text = "${item.startHour} - ${item.endHour}"
                parkingLotOpenDays.text = root.resources.getString(R.string.parking_lot_working_days)
                parkingLotOpenDays.colorWorkingDays(item.days, Color.RED)
                availabilityIndicator.setTemporaryClosedMode(false)
                availabilityIndicator.changeLevel(item.occupancyLevel)

                /** Non Stop */
                if (item.isNonStop == true) {
                    parkingLotOpenHours.text = root.resources.getString(R.string.parking_lot_24_7)
                    parkingLotOpenDays.text = ""

                /** Temporary Closed */
                } else if (item.isClosed == true) {
                    availabilityIndicator.setTemporaryClosedMode(true)
                    parkingLotOpenDays.text = ""
                    parkingLotOpenHours.text = root.resources.getString(R.string.parking_lot_not_available)
                }
            }
        }
    }
}
