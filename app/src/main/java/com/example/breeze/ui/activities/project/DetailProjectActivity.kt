package com.example.breeze.ui.activities.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.project.DataProject
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
        val dataProject: DataProject? = intent.getParcelableExtra(STORY_INTENT_DATA)

        dataProject?.let { displayProjectDetails(it) }
    }
    private fun displayProjectDetails(dataProject: DataProject) {
        with(binding) {
            Glide.with(this@DetailProjectActivity)
                .load(dataProject.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(ivEvent)

            tvTitle.text = dataProject.title
            tvLocation.text = dataProject.location
            tvCarbon.text = "${dataProject.totalCo2?.div(1000)}"
            tvHarga.text = "Rp. ${dataProject.price}"
            tvTree.text = "/ Tree"
            tvDesc.text = dataProject.description
            tvBennefit.text = "${dataProject.benefits}"
        }
    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}