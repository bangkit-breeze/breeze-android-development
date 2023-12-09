package com.example.breeze.ui.activities.details.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.event.DataEvent
import com.example.breeze.databinding.ActivityDetailEventBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        val storyData = intent.getParcelableExtra<DataEvent>(STORY_INTENT_DATA)

        storyData?.let {
            binding.tvPoints.text = it.rewardPoints.toString()
            binding.tvTitle.text = it.name
            binding.tvLocation.text = it.location
            binding.tvDesc.text = it.description
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val startDate = inputFormat.parse(it.startAt)
            val formattedDate = outputFormat.format(startDate)

            binding.tvDate.text = formattedDate


            Glide.with(this)
                .load(it.eventImageUrl)
                .into(binding.ivEvent)
        }
    }

    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}