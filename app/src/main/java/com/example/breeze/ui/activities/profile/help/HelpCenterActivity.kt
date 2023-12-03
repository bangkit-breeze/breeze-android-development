package com.example.breeze.ui.activities.profile.help

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.breeze.R
import com.example.breeze.databinding.ActivityDetailProfileBinding
import com.example.breeze.databinding.ActivityHelpCenterBinding
import com.example.breeze.ui.activities.profile.EditProfileActivity

class HelpCenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnFaqs.setOnClickListener {
            showToast(resources.getString(R.string.feature_not_yet))
        }
        binding.btnChat.setOnClickListener {
            showToast(resources.getString(R.string.feature_not_yet))
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}