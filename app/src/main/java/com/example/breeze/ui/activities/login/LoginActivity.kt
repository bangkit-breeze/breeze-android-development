package com.example.breeze.ui.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.breeze.R
import com.example.breeze.databinding.ActivityLoginBinding
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.activities.register.RegisterActivity
import com.example.breeze.ui.factory.AuthViewModelFactory
import com.example.breeze.ui.viewmodel.LoginViewModel
import com.example.breeze.utils.AnimationUtils
import com.example.breeze.utils.constans.Constants
import com.example.breeze.utils.dialog.DialogUtils
import com.example.breeze.utils.dialog.ProgressDialogUtils
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.SnackbarUtils

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListeners()
        playAnimations()
    }
    private fun setupClickListeners() = with(binding) {
            tvLoginToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            btnLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
            btnLogin.setOnClickListener { handleLogin() }
    }

    private fun handleLogin() {
        val (email, password) = binding.run {
            arrayOf(
                etEmailLogin.text.toString().trim(),
                etPasswordLogin.text.toString().trim()
            )
        }
        when {
            arrayOf(email, password).any {
                it.isEmpty()
                    } -> {
                        showSnackBar(
                            when {
                                email.isEmpty() -> R.string.email_required
                                else -> R.string.password_required
                            }
                    )
            }
            else -> viewModel.login(email, password).observe(this@LoginActivity) { result ->
                when (result) {
                    is Result.Loading -> ProgressDialogUtils.showProgressDialog(this@LoginActivity)
                    is Result.Success -> onLoginSuccess()
                    is Result.Error -> onLoginError(result.error)
                }
            }
        }
    }
    private fun onLoginSuccess() {
        ProgressDialogUtils.hideProgressDialog()
        DialogUtils.showCustomDialog(this@LoginActivity, R.layout.alert_dialog_success) { alertDialogBuilder, tvDescription ->
            tvDescription.text = getString(R.string.login_success_desc)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getUserLogin().observe(this) {
                if (it.token.isNotEmpty()) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }, Constants.DIALOG_DELAY)
    }
    private fun onLoginError(errorMessage: String) {
        ProgressDialogUtils.hideProgressDialog()
        showSnackBar(errorMessage)
    }
    private fun playAnimations() {
        with(binding) {
            AnimationUtils.playSequentialFadeInAnimations(
                tvTitle,
                tvTitle2,
                tvEmail,
                emailEditTextLayout,
                tvPassword,
                passwordEditTextLayout,
                btnLogin,
                textViewToRegister,
                duration = Constants.DURATION_ANIMATION_DELAY
            )
        }
    }
    private fun showSnackBar(messageResId: Any) {
        SnackbarUtils.showWithDismissAction(binding.root, messageResId)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}