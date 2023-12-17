package com.example.breeze.ui.activities.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.breeze.R
import com.example.breeze.databinding.ActivityDetailProjectBinding
import com.example.breeze.databinding.ActivityProjectBinding

class DetailProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}