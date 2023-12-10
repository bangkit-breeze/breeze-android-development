package com.example.breeze.ui.activities.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.DataTrackFood
import com.example.breeze.databinding.ActivityDetailResultCarbonFoodBinding
import com.example.breeze.databinding.ActivityResultCameraFoodBinding
import com.example.breeze.ui.adapter.FoodCarbonAdapter
import com.example.breeze.ui.adapter.LeaderBoardAdapter
import kotlin.math.roundToInt

class DetailResultCarbonFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultCarbonFoodBinding
    private val adapter = FoodCarbonAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailResultCarbonFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<DataTrackFood>(STORY_INTENT_DATA)
        if (data != null) {
            val foodName = data.predictResult?.foodName
            binding.tvTitle.text = foodName
            Glide.with(this)
                .load(data.imageUrl)
                .into(binding.myImageView)
            val acuracy = data.predictResult?.confidence?.toFloat()
            if (acuracy != null) {
                if(acuracy < 0.6 && acuracy > 0.5){
                    binding.tvAccuacy.text = "Low Accuracy"
                }else if(acuracy < 0.8 && acuracy > 0.7){
                    binding.tvAccuacy.text = "Medium Accuracy"
                }else if(acuracy > 0.9){
                    binding.tvAccuacy.text = "High Accuracy"
                }
            }

            val emision = data.predictResult?.totalEmissions?.toFloat()
            val totalEmision = emision?.let { (it * 1000).roundToInt() } ?: 0
            binding.tvCarbon.text = "${data.predictResult?.totalEmissions} kgCO2e"
            val layoutManager = LinearLayoutManager(this)
            binding.rvIngredients.layoutManager = layoutManager
            binding.rvIngredients.adapter = adapter
            adapter.submitList(data.predictResult?.ingredients)
        }

    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }

}