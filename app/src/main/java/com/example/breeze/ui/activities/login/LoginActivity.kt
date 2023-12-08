package com.example.breeze.ui.activities.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleObserver
import com.example.breeze.R
import com.example.breeze.databinding.ActivityLoginBinding
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.activities.register.RegisterActivity
import com.example.breeze.ui.factory.AuthViewModelFactory
import com.example.breeze.utils.Constants
import com.example.breeze.utils.Result

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }
    private lateinit var progressDialog: Dialog
    private lateinit var alertDialog: AlertDialog.Builder
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.tvLoginToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin(){
        val email = binding.etEmailLogin.text.toString().trim()
        val password = binding.etPasswordLogin.text.toString().trim()


        viewModel.login(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading ->   showProgressDialog()
                is Result.Success -> onLoginSuccess()
                is Result.Error -> onLoginError(result.error)
            }
        }
    }

    private fun onLoginSuccess() {

        hideProgressDialog()
        showSuccessDialog()
        handler.postDelayed({
            viewModel.getUserLogin().observe(this) {
                if (it.token.isNotEmpty()) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }, 3000)

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
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
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
        val fadeInDuration = Constants.DURATION_ANIMATION_DELAY
        val titleAnimator = createFadeInAnimator(binding.tvTitle, fadeInDuration)
        val messageAnimator = createFadeInAnimator(binding.tvTitle2, fadeInDuration)
        val emailTextAnimator = createFadeInAnimator(binding.tvEmail, fadeInDuration)
        val emailEditTextAnimator = createFadeInAnimator(binding.emailEditTextLayout, fadeInDuration)
        val passwordTextAnimator = createFadeInAnimator(binding.tvPassword, fadeInDuration)
        val passwordEditTextAnimator = createFadeInAnimator(binding.passwordEditTextLayout, fadeInDuration)
        val loginButtonAnimator = createFadeInAnimator(binding.btnLogin, fadeInDuration)
        val registerTextAnimator = createFadeInAnimator(binding.textViewToRegister, fadeInDuration)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                messageAnimator,
                emailTextAnimator,
                emailEditTextAnimator,
                passwordTextAnimator,
                passwordEditTextAnimator,
                loginButtonAnimator,
                registerTextAnimator
            )
            startDelay = fadeInDuration
        }.start()
    }

    private fun createFadeInAnimator(view: View, duration: Long): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(duration)
    }
}