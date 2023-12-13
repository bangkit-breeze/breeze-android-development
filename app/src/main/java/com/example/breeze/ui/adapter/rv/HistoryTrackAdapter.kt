package com.example.breeze.ui.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.breeze.R
import com.example.breeze.data.model.response.user.DataHistoryTrack
import com.example.breeze.databinding.ItemCarbonBinding
import com.example.breeze.utils.number.DateUtils.formatDateWithMonthName
import com.example.breeze.utils.number.NumberUtils.calculateEmission

class HistoryTrackAdapter : ListAdapter<DataHistoryTrack, HistoryTrackAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemCarbonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataHistoryTrack) = with(binding) {
            tvTitle.text = data.name
            tvCarbon.text = "${calculateEmission(data.totalEmission)} kgCO2e"
            tvDate.text = data.createdAt?.let { formatDateWithMonthName(it) }
            ivIcon.setImageResource(if (data.category == "FOOD") R.drawable.ic_food_black else R.drawable.ic_vehicle_black)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            ItemCarbonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataHistoryTrack>() {
            override fun areItemsTheSame(oldItem: DataHistoryTrack, newItem: DataHistoryTrack): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataHistoryTrack, newItem: DataHistoryTrack): Boolean {
                return oldItem == newItem
            }
        }
    }
}