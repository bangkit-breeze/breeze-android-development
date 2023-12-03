package com.example.breeze.ui.activities.profile.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.breeze.R
import com.example.breeze.databinding.ActivityExchangeTokenBinding
import com.example.breeze.databinding.ActivityHelpCenterBinding

class ExchangeTokenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExchangeTokenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.btnSubscribe.setOnClickListener {
            showToast(resources.getString(R.string.feature_not_yet))
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}