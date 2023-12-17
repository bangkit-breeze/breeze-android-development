package com.example.breeze.ui.activities.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.breeze.R
import com.example.breeze.databinding.ActivityDetailProfileBinding
import com.example.breeze.databinding.ActivityEditPasswordBinding
import com.example.breeze.utils.SnackbarUtils

class EditPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.btnUpdate.setOnClickListener {
            SnackbarUtils.showWithDismissAction(binding.root, R.string.feature_not_yet)
        }
    }
}