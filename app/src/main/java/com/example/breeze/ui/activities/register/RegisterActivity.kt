package com.example.breeze.ui.activities.register

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
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.example.breeze.R
import com.example.breeze.databinding.ActivityRegisterBinding
import com.example.breeze.ui.activities.login.LoginActivity
import com.example.breeze.ui.factory.AuthViewModelFactory
import com.example.breeze.utils.AnimationUtils
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
    private fun setupClickListeners() = with(binding) {
        tvRegisterToLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
        btnRegister.setOnClickListener { handleRegister() }
    }
    private fun handleRegister() {
        val (name, email, password, passwordConfirm) = binding.run {
            arrayOf(
                etRegisterName.text.toString().trim(),
                etRegiterEmail.text.toString().trim(),
                etRegisterPassword.text.toString().trim(),
                etRegisterConfirmPassword.text.toString().trim()
            )
        }
        when {
            password != passwordConfirm || arrayOf(name, email, password, passwordConfirm).any { it.isEmpty() } -> {
                showToast(
                    when {
                        password != passwordConfirm -> R.string.password_notmatch
                        name.isEmpty() -> R.string.fullName_required
                        email.isEmpty() -> R.string.email_required
                        password.isEmpty() -> R.string.password_required
                        else -> R.string.confirmPassword_required
                    }
                )
            }
            else -> viewModel.register(name, email, password, passwordConfirm).observe(this) { result ->
                when (result) {
                    is Result.Loading -> showProgressDialog()
                    is Result.Success -> onRegisterSuccess()
                    is Result.Error -> onRegisterError(result.error)
                }
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
        }, Constants.DIALOG_DELAY)
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
        AnimationUtils.playSequentialFadeInAnimations(
            binding.tvTitle,
            binding.tvTitle2,
            binding.tvName,
            binding.nameEditTextLayout,
            binding.tvEmail,
            binding.emailEditTextLayout,
            binding.tvPassword,
            binding.passwordEditTextLayout,
            binding.tvConfirmPassword,
            binding.confirmPasswordEditTextLayout,
            binding.btnRegister,
            binding.textViewToLogin,
            duration = Constants.DURATION_ANIMATION_DELAY
        )
    }
    private fun showToast(messageResId: Int) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show()
    }

}