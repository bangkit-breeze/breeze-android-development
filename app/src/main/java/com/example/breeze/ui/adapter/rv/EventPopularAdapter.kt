package com.example.breeze.ui.adapter.rv

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.event.DataEvent
import com.example.breeze.databinding.ItemEventPopulerBinding
import com.example.breeze.ui.activities.event.DetailEventActivity

class EventPopularAdapter: ListAdapter<DataEvent, EventPopularAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemEventPopulerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataEvent) = with(binding) {
            tvTitle.text = data.name
            tvDesc.text = data.description
            tvPoint.text = data.rewardPoints.toString()
            tvLocation.text = data.location


            Glide.with(itemView.context)
                .load(data.eventImageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(ivEvent)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailEventActivity::class.java).apply {
                    putExtra(DetailEventActivity.STORY_INTENT_DATA, data)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            ItemEventPopulerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataEvent>() {
            override fun areItemsTheSame(oldItem: DataEvent, newItem: DataEvent): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataEvent, newItem: DataEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}