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
    private val userRole: UserRole,
    private val onListItemClickListener: (ParkingLot) -> Unit
) : ListAdapter<ParkingLot, ParkingLotsAdapter.ViewHolder>(ListItemCallback()) {

//    private lateinit var binding: ItemParkingLotBinding

    class ListItemCallback : DiffUtil.ItemCallback<ParkingLot>() {
        override fun areItemsTheSame(oldItem: ParkingLot, newItem: ParkingLot): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ParkingLot, newItem: ParkingLot): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParkingLotBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemParkingLotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ParkingLot) {
            binding.apply {
                /** Name */
                parkingLotName.text = item.name

                /** Open Hours */
                parkingLotOpenHours.text = if (item.isNonStop == true) {
                    root.resources.getString(R.string.parking_lot_24_7)
                } else if (item.isClosed == true) {
                    root.resources.getString(R.string.parking_lot_not_available)
                } else {
                    "${item.startHour} - ${item.endHour}"
                }

                /** Open Days - visibility */
                parkingLotOpenDays.isVisible = !(item.isClosed == true || item.isNonStop == true)

                /** Open Days - Black color for working days, Red color not working days */
                item.isNonStop?.let {
                    parkingLotOpenDays.colorWorkingDays(item.days, Color.RED, it)
                }

                /** Availability Indicator */
                availabilityIndicator.setTemporaryClosedMode(item.isClosed == true)

                /** Server can return unacceptable value (more than 100%), we need to insure value */
                availabilityIndicator.changeLevel(
                    if (item.occupiedSeats > 100) {
                        100
                    } else {
                        item.occupiedSeats.toInt()
                    }
                )

                /** ArrowIcon */
                parkingLotArrowIcon.isVisible = !(userRole == UserRole.REGULAR && item.isClosed == true)

                /** ClickListener */
                if (userRole == UserRole.ADMIN || item.isClosed == false) {
                    root.setOnClickListener { onListItemClickListener(item) }
                }
            }
        }
    }
}
