package com.example.breeze.ui.activities.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.breeze.R
import com.example.breeze.data.model.DataTrackFood
import com.example.breeze.databinding.ActivityDetailResultCarbonFoodBinding
import com.example.breeze.databinding.ActivityResultCameraFoodBinding

class DetailResultCarbonFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultCarbonFoodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailResultCarbonFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<DataTrackFood>(STORY_INTENT_DATA)
        if (data != null) {
            val foodName = data.predictResult?.foodName
            binding.tvTitle.text = foodName
        }

    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }

}