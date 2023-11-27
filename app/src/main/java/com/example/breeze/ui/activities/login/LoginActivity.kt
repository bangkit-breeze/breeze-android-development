package com.example.breeze.ui.activities.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleObserver
import com.example.breeze.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}