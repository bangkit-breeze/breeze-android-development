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
import com.example.breeze.data.model.response.project.DataProject
import com.example.breeze.databinding.ItemArticleBinding
import com.example.breeze.databinding.ItemProjectBinding
import com.example.breeze.ui.activities.article.DetailArticleActivity
import com.example.breeze.ui.activities.project.DetailProjectActivity

class ProjectAdapter : ListAdapter<DataProject, ProjectAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataProject) = with(binding) {
            tvTitle.text = data.title
            tvLocation.text = data.location
            tvPoints.text = "Rp. ${data.price}"
            tvCarbon.text = "${data.totalCo2?.div(1000)}"

            Glide.with(itemView.context)
                .load(data.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(ivEvent)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailProjectActivity::class.java).apply {
                    putExtra(DetailProjectActivity.STORY_INTENT_DATA, data)
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
            ItemProjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataProject>() {
            override fun areItemsTheSame(oldItem: DataProject, newItem: DataProject): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataProject, newItem: DataProject): Boolean {
                return oldItem == newItem
            }
        }
    }
}