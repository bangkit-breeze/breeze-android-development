package com.example.breeze.ui.activities.register

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
import android.widget.Toast
import androidx.activity.viewModels
import com.example.breeze.R
import com.example.breeze.databinding.ActivityRegisterBinding
import com.example.breeze.ui.activities.login.LoginActivity
import com.example.breeze.ui.factory.AuthViewModelFactory
import com.example.breeze.utils.Constants
import com.example.breeze.utils.Result



class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }
    private lateinit var progressDialog: Dialog
    private lateinit var alertDialog: AlertDialog.Builder
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alertDialog = AlertDialog.Builder(this@RegisterActivity)
        setupClickListeners()
        playAnimations()
    }
    private fun setupClickListeners() {
        binding.tvRegisterToLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
        binding.btnRegister.setOnClickListener {
            handleRegister()
        }
    }

    private fun handleRegister() {
        val name = binding.etRegisterName.text.toString().trim()
        val email = binding.etRegiterEmail.text.toString().trim()
        val password = binding.etRegisterPassword.text.toString().trim()
        val passwordConfirm = binding.etRegisterConfirmPassword.text.toString().trim()
        if (password != passwordConfirm) {
            Toast.makeText(this, getString(R.string.password_notmatch), Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.register(name, email, password, passwordConfirm).observe(this) { result ->
            when (result) {
                is Result.Loading ->  showProgressDialog()
                is Result.Success -> onRegisterSuccess()
                is Result.Error -> onRegisterError(result.error)
            }
        }

    }
    private fun onRegisterError(errorMessage: String) {
        hideProgressDialog()
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
    private fun showProgressDialog() {
        progressDialog = Dialog(this@RegisterActivity)
        progressDialog.setContentView(R.layout.custom_progressbar)
        val progressBar: ProgressBar = progressDialog.findViewById(R.id.progressBar)
        progressBar.isIndeterminate = true
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.show()
    }
    private fun onRegisterSuccess() {
        hideProgressDialog()
        showSuccessDialog()
        handler.postDelayed({
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }, 3000)
    }

    private fun showSuccessDialog() {
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_success, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        handler.postDelayed({
            alertDialog.dismiss()
        }, 3000)
    }


    private fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
    private fun playAnimations() {
        val fadeInDuration = Constants.DURATION_ANIMATION_DELAY
        val titleAnimator = createFadeInAnimator(binding.tvTitle, fadeInDuration)
        val titleAnimator2 = createFadeInAnimator(binding.tvTitle2, fadeInDuration)
        val nameTextAnimator = createFadeInAnimator(binding.tvName, fadeInDuration)
        val nameEditTextAnimator = createFadeInAnimator(binding.nameEditTextLayout, fadeInDuration)
        val emailTextAnimator = createFadeInAnimator(binding.tvEmail, fadeInDuration)
        val emailEditTextAnimator = createFadeInAnimator(binding.emailEditTextLayout, fadeInDuration)
        val passwordTextAnimator = createFadeInAnimator(binding.tvPassword, fadeInDuration)
        val passwordEditTextAnimator = createFadeInAnimator(binding.passwordEditTextLayout, fadeInDuration)
        val passwordConfirmTextAnimator = createFadeInAnimator(binding.tvConfirmPassword, fadeInDuration)
        val passwordConfirmEditTextAnimator = createFadeInAnimator(binding.confirmPasswordEditTextLayout, fadeInDuration)
        val registerButtonAnimator = createFadeInAnimator(binding.btnRegister, fadeInDuration)
        val registerTextAnimator = createFadeInAnimator(binding.textViewToLogin, fadeInDuration)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                titleAnimator2,
                nameTextAnimator,
                nameEditTextAnimator,
                emailTextAnimator,
                emailEditTextAnimator,
                passwordTextAnimator,
                passwordEditTextAnimator,
                passwordConfirmTextAnimator,
                passwordConfirmEditTextAnimator,
                registerButtonAnimator,
                registerTextAnimator
            )
            startDelay = fadeInDuration
        }.start()
    }

    private fun createFadeInAnimator(view: View, duration: Long): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(duration)
    }
}