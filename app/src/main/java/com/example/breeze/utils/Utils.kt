package com.example.breeze.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

fun showToast(context: Context, message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showToastString(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showLoading(progressBar: ProgressBar, isLoading: Boolean) {
    progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
}