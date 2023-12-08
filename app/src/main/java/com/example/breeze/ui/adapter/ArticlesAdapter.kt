package com.example.breeze.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.DataArticle
import com.example.breeze.databinding.ItemArticleBinding
import com.example.breeze.ui.activities.article.DetailArticleActivity

class ArticlesAdapter : ListAdapter<DataArticle, ArticlesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataArticle) = with(binding) {
            tvTitle.text = data.title
            tvDesc.text = data.description

            Glide.with(itemView.context)
                .load(data.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(ivArticle)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailArticleActivity::class.java).apply {
                    putExtra(DetailArticleActivity.STORY_INTENT_DATA, data)
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
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataArticle>() {
            override fun areItemsTheSame(oldItem: DataArticle, newItem: DataArticle): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataArticle, newItem: DataArticle): Boolean {
                return oldItem == newItem
            }
        }
    }
}