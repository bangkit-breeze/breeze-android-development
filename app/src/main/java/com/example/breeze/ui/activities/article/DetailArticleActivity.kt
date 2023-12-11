package com.example.breeze.ui.activities.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.breeze.data.model.response.article.DataArticle
import com.example.breeze.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.settings.javaScriptEnabled = true
        val dataArticle = intent.getParcelableExtra<DataArticle>(STORY_INTENT_DATA)
        if (dataArticle != null && !dataArticle.contentUrl.isNullOrBlank()) {
            binding.webView.loadUrl(dataArticle.contentUrl)
        } else {
            Toast.makeText(this, "Data tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}