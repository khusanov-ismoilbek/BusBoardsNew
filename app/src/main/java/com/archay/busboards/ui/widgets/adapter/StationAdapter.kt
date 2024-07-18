package com.archay.busboards.ui.widgets.adapter

import android.content.res.Resources
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.archay.busboards.databinding.ItemStationBinding
import com.archay.busboards.ui.BusViewModel

class StationAdapter :
    ListAdapter<BusViewModel.StationDto, StationAdapter.StationViewHolder>(StationDiffUtil) {

    object StationDiffUtil : ItemCallback<BusViewModel.StationDto>() {
        override fun areItemsTheSame(
            oldItem: BusViewModel.StationDto,
            newItem: BusViewModel.StationDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BusViewModel.StationDto,
            newItem: BusViewModel.StationDto
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class StationViewHolder(private val binding: ItemStationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            if (absoluteAdapterPosition == 0) {
                binding.tvStationName.setTextSize(TypedValue.COMPLEX_UNIT_SP,40f)
                binding.tvStationName.setTypeface(binding.tvStationName.typeface, Typeface.BOLD)
            } else {
                binding.tvStationName.setTextSize(TypedValue.COMPLEX_UNIT_SP,40f)
                binding.tvStationName.setTypeface(binding.tvStationName.typeface, Typeface.NORMAL)
            }
            binding.tvStationName.text = getItem(absoluteAdapterPosition).stationName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        return StationViewHolder(
            ItemStationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind()
    }


}