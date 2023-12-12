package com.example.breeze.ui.activities.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.user.UserProfileResponse
import com.example.breeze.databinding.ActivityDetailProfileBinding
import com.example.breeze.ui.factory.ProfileViewModelFactory
import com.example.breeze.ui.viewmodel.ProfileViewModel
import com.example.breeze.utils.Result

class DetailProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileBinding
    private val  viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnChangeProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }


        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this, EditPasswordActivity::class.java)
            startActivity(intent)
        }
        setupViews()
    }
    private fun setupViews() {
        viewModel.getUserLogin().observe(this) {
            dataUser = it
        }
    }
    private fun handleProfile(result: Result<UserProfileResponse>) {
        when (result) {
            is Result.Loading -> return
            is Result.Success -> {
                val userProfile = result.data.dataUser
                if (userProfile != null) {
                    binding.tvName.text = userProfile.fullName.toString()
                    binding.tvEmail.text = userProfile.email.toString()
                    Glide.with(this)
                        .load(userProfile.avatarUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .fallback(R.drawable.ic_launcher_background)
                        .into(binding.ivPicture)
                }
            }
            is Result.Error -> {
                val message = result.error
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getProfile(dataUser.token).observe(this) {
            handleProfile(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}