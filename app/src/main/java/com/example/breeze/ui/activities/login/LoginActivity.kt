package com.example.breeze.ui.activities.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleObserver
import com.example.breeze.R
import com.example.breeze.databinding.ActivityLoginBinding
import com.example.breeze.utils.Constants

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimations()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
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