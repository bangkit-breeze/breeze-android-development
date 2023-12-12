package com.example.breeze.utils

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}