package com.example.breeze.ui.adapter.rv

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.article.DataArticle
import com.example.breeze.data.model.response.user.DataHistoryTrack
import com.example.breeze.databinding.ItemArticleBinding
import com.example.breeze.databinding.ItemCarbonBinding
import com.example.breeze.ui.activities.article.DetailArticleActivity
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryTrackAdapter : ListAdapter<DataHistoryTrack, HistoryTrackAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemCarbonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataHistoryTrack) = with(binding) {
            tvTitle.text = data.name
            tvCarbon.text = ((data.totalEmission?.toFloat() ?: 0f) / 1000.0).toString()
            tvDate.text = data.createdAt?.let { formatTimestampToCustomDate(it) }


        }
        fun formatTimestampToCustomDate(timestamp: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val date = inputFormat.parse(timestamp)
            return outputFormat.format(date)
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