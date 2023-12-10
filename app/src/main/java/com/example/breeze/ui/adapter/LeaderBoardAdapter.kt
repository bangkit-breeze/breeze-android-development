package com.example.breeze.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
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
            // Assuming data.rank is the user's rank
            when (data.rank) {
                1 -> {
                    binding.rank1.visibility = View.VISIBLE
                    binding.rank2.visibility = View.GONE
                    binding.rank3.visibility = View.GONE
                    binding.tvRanking.visibility = View.GONE
                }
                2 -> {
                    binding.rank1.visibility = View.GONE
                    binding.rank2.visibility = View.VISIBLE
                    binding.rank3.visibility = View.GONE
                    binding.tvRanking.visibility = View.GONE
                }
                3 -> {
                    binding.rank1.visibility = View.GONE
                    binding.rank2.visibility = View.GONE
                    binding.rank3.visibility = View.VISIBLE
                    binding.tvRanking.visibility = View.GONE
                }
                else -> {
                    binding.rank1.visibility = View.GONE
                    binding.rank2.visibility = View.GONE
                    binding.rank3.visibility = View.GONE
                    binding.tvRanking.visibility = View.VISIBLE
                    binding.tvRanking.text = data.rank.toString()
                }
            }

            tvTitle.text = data.fullName.toString()
            tvExp.text = "${data.exp} exp"

            Glide.with(itemView.context)
                .load(data.avatarUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
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