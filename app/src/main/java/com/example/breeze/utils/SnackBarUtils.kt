package com.example.breeze.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.example.breeze.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    fun showWithDismissAction(view: View, message: Any) {
        val messageText = when (message) {
            is Int -> view.context.getString(message)
            is String -> message
            else -> throw IllegalArgumentException("Unsupported message type")
        }
        Snackbar.make(view, messageText, Snackbar.LENGTH_LONG)
            .apply {
                setActionTextColor(ContextCompat.getColor(view.context, R.color.light_primary))
                setAction("Dismiss") { dismiss() }
            }
            .show()
    }
}
