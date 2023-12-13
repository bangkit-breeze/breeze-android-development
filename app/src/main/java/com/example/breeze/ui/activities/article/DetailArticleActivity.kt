package com.example.breeze.ui.activities.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.breeze.data.model.response.article.DataArticle
import com.example.breeze.databinding.ActivityDetailArticleBinding
import com.example.breeze.utils.showToastString

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.webView.settings) {
            javaScriptEnabled = true
        }

        intent.getParcelableExtra<DataArticle>(STORY_INTENT_DATA)?.let { dataArticle ->
            if (!dataArticle.contentUrl.isNullOrBlank()) {
                binding.webView.loadUrl(dataArticle.contentUrl)
            } else {
                showToast("Data tidak valid")
            }
        } ?: showToast("Data tidak valid")
    }

    private fun showToast(message: String) {
        showToastString(this, message)
    }

    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}