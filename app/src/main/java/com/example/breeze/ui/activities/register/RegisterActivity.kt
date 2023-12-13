package com.example.breeze.ui.activities.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.breeze.R
import com.example.breeze.databinding.ActivityRegisterBinding
import com.example.breeze.ui.activities.login.LoginActivity
import com.example.breeze.ui.factory.AuthViewModelFactory
import com.example.breeze.ui.viewmodel.RegisterViewModel
import com.example.breeze.utils.animation.AnimationUtils
import com.example.breeze.utils.constans.Constants
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.SnackbarUtils
import com.example.breeze.utils.dialog.DialogUtils
import com.example.breeze.utils.dialog.ProgressDialogUtils

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                showSnackBar(
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
                    is Result.Loading -> ProgressDialogUtils.showProgressDialog(this@RegisterActivity)
                    is Result.Success -> onRegisterSuccess()
                    is Result.Error -> onRegisterError(result.error)
                }
            }
        }
    }
    private fun onRegisterError(errorMessage: String) {
        ProgressDialogUtils.hideProgressDialog()
        showSnackBar(errorMessage)
    }

    private fun onRegisterSuccess() {
        ProgressDialogUtils.hideProgressDialog()
        DialogUtils.showCustomDialog(this@RegisterActivity, R.layout.alert_dialog_success) { alertDialogBuilder, tvDescription ->
            tvDescription.text = getString(R.string.register_success_desc)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }, Constants.DIALOG_DELAY)
    }


    private fun playAnimations() {
        with(binding) {
            AnimationUtils.playSequentialFadeInAnimations(
                tvTitle,
                tvTitle2,
                tvName,
                nameEditTextLayout,
                tvEmail,
                emailEditTextLayout,
                tvPassword,
                passwordEditTextLayout,
                tvConfirmPassword,
                confirmPasswordEditTextLayout,
                btnRegister,
                textViewToLogin,
                duration = Constants.DURATION_ANIMATION_DELAY
            )
        }
    }

    private fun showSnackBar(messageResId: Any) {
        SnackbarUtils.showWithDismissAction(binding.root, messageResId)
    }

}