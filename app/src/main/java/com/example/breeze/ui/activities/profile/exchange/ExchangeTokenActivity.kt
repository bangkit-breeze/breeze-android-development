package com.example.breeze.ui.activities.profile.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.user.UserProfileResponse
import com.example.breeze.databinding.ActivityExchangeTokenBinding
import com.example.breeze.ui.factory.ProfileViewModelFactory
import com.example.breeze.ui.viewmodel.ProfileViewModel
import com.example.breeze.utils.constans.Result

class ExchangeTokenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExchangeTokenBinding
    private val  viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
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
        setupViews()
    }
    private fun setupViews() {
        viewModel.getToken().observe(this) {
            dataUser = it
        }
    }
    private fun handleProfile(result: Result<UserProfileResponse>) {
        when (result) {
            is Result.Loading -> return
            is Result.Success -> {
                val userProfile = result.data.dataUser
                if (userProfile != null) {
                    binding.tvPoints.text = userProfile.points.toString()
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