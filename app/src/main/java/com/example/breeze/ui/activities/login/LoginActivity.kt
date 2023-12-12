package com.example.breeze.ui.activities.login

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.breeze.R
import com.example.breeze.databinding.ActivityLoginBinding
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.activities.register.RegisterActivity
import com.example.breeze.ui.factory.AuthViewModelFactory
import com.example.breeze.ui.viewmodel.LoginViewModel
import com.example.breeze.utils.AnimationUtils
import com.example.breeze.utils.Constants
import com.example.breeze.utils.Result
import com.example.breeze.utils.SnackbarUtils
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }
    private lateinit var progressDialog: Dialog
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var binding: ActivityLoginBinding
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(binding.root)
        alertDialog = AlertDialog.Builder(this@LoginActivity)
        setupClickListeners()
        playAnimations()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun setupClickListeners() {
        with(binding) {
            tvLoginToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            btnLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
            btnLogin.setOnClickListener { handleLogin() }
        }
    }
    private fun handleLogin() {
        val (email, password) = binding.run {
            arrayOf(
                etEmailLogin.text.toString().trim(),
                etPasswordLogin.text.toString().trim()
            )
        }
        when {
            arrayOf(email, password).any { it.isEmpty() } -> {
                showSnackBar(
                    when {
                        email.isEmpty() -> R.string.email_required
                        else -> R.string.password_required
                    }
                )
            }
            else -> viewModel.login(email, password).observe(this@LoginActivity) { result ->
                when (result) {
                    is Result.Loading -> showProgressDialog()
                    is Result.Success -> onLoginSuccess()
                    is Result.Error -> onLoginError(result.error)
                }
            }
        }
    }
    private fun onLoginSuccess() {
        hideProgressDialog()
        showSuccessDialog()
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getUserLogin().observe(this) {
                if (it.token.isNotEmpty()) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }, Constants.DIALOG_DELAY)
    }
    private fun showSuccessDialog() {
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_success, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        viewModel.getUserLogin().observe(this) {
            val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
            tvDescription.text = getString(R.string.login_success_desc)
        }
    }
    private fun onLoginError(errorMessage: String) {
        hideProgressDialog()
        showSnackBar(errorMessage)
    }
    private fun showSnackBar(messageResId: Any) {
        SnackbarUtils.showWithDismissAction(rootView, messageResId)
    }
    private fun showProgressDialog() {
        progressDialog = Dialog(this@LoginActivity)
        progressDialog.setContentView(R.layout.custom_progressbar)
        val progressBar: ProgressBar = progressDialog.findViewById(R.id.progressBar)
        progressBar.isIndeterminate = true
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.show()
    }
    private fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
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
}