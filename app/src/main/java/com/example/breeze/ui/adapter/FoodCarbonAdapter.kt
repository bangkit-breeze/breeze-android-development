package com.example.breeze.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.breeze.data.model.response.track.IngredientsItem
import com.example.breeze.databinding.ItemIngredientsBinding

class FoodCarbonAdapter: ListAdapter<IngredientsItem, FoodCarbonAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: IngredientsItem) = with(binding) {
            val berat = ((data.berat?.toFloat() ?: 0f) * 1000).toInt()
            tvTitle.text = "$berat, ${data.bahan}"
            tvEmisi.text = "${data.carbonFootprint} kgCO2e"

        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            ItemIngredientsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<IngredientsItem>() {
            override fun areItemsTheSame(oldItem: IngredientsItem, newItem: IngredientsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: IngredientsItem, newItem: IngredientsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}