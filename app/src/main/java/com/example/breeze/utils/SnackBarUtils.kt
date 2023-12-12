package com.example.breeze.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.example.breeze.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    fun showWithDismissAction(view: View, messageResId: Int) {
        Snackbar.make(view, messageResId, Snackbar.LENGTH_LONG)
            .apply {
                setActionTextColor(ContextCompat.getColor(view.context, R.color.light_primary))
                setAction("Dismiss") { dismiss() }
            }
            .show()
    }
}