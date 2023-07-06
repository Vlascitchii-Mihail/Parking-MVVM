package com.endava.parking.ui.spotslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.endava.parking.R
import com.endava.parking.data.model.Spot
import com.endava.parking.data.model.SpotType
import com.endava.parking.databinding.ParkingLotItemBinding

class SpotsAdapter(private val click: (spot: Spot) -> Unit) :
    ListAdapter<Spot, SpotsAdapter.ParkingLotViewHolder>(SpotsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingLotViewHolder =
        ParkingLotViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ParkingLotViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }

    class ParkingLotViewHolder(private val binding: ParkingLotItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Spot, click: (spot: Spot) -> Unit) {
            with(binding) {
                flImgContainer.isHovered = item.busy
                (flImgContainer.getChildAt(FIRST_CHILD) as ImageView).setImageResource(getSpotImageId(item.spotType))
                tvSpotName.text = item.spotName

                if (item.spotType != SpotType.TEMPORARY_CLOSED && !item.busy) {
                    root.setOnClickListener { click(item) }
                }
            }
        }

        companion object {
            fun inflateFrom(parent: ViewGroup): ParkingLotViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ParkingLotItemBinding.inflate(layoutInflater, parent, false)
                return ParkingLotViewHolder(binding)
            }

            fun getSpotImageId(spotType: SpotType): Int {
                return when (spotType) {
                    SpotType.FAMILY -> R.drawable.stroller_10
                    SpotType.DISABLED_PERSON -> R.drawable.disabled
                    SpotType.REGULAR -> R.color.transparent
                    SpotType.TEMPORARY_CLOSED -> R.drawable.block_10
                }
            }

            const val FIRST_CHILD = 0
        }
    }
}

class SpotsDiffCallback: DiffUtil.ItemCallback<Spot>() {

    override fun areItemsTheSame(oldItem: Spot, newItem: Spot) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Spot, newItem: Spot): Boolean =
        oldItem == newItem
}
