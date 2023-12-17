package com.example.breeze.ui.activities.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.breeze.R
import com.example.breeze.databinding.ActivityEditPasswordBinding
import com.example.breeze.databinding.ActivityEditProfileBinding
import com.example.breeze.utils.SnackbarUtils

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.btnUpdate.setOnClickListener {
            SnackbarUtils.showWithDismissAction(binding.root, R.string.feature_not_yet)
        }
    }
}