package com.example.breeze.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.LeaderboardItem
import com.example.breeze.data.model.event.DataEvent
import com.example.breeze.databinding.ItemEventBinding
import com.example.breeze.databinding.ItemLeaderboardBinding
import com.example.breeze.ui.activities.details.events.DetailEventActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class LeaderBoardAdapter: ListAdapter<LeaderboardItem, LeaderBoardAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LeaderboardItem) = with(binding) {
            tvRanking.text = data.rank.toString()
            tvTitle.text = data.fullName.toString()
            tvExp.text = data.exp.toString()

            Glide.with(itemView.context)
                .load(data.avatarUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(ivPicture)
            
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            ItemLeaderboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LeaderboardItem>() {
            override fun areItemsTheSame(oldItem: LeaderboardItem, newItem: LeaderboardItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: LeaderboardItem, newItem: LeaderboardItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}