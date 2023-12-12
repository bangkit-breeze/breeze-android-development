package com.example.breeze.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.example.breeze.R

object DialogUtils {
    fun showCustomDialog(context: Context, layoutResId: Int, configure: (AlertDialog.Builder, TextView) -> Unit) {
        val customDialogView = LayoutInflater.from(context).inflate(layoutResId, null)
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(customDialogView)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Mendapatkan referensi ke TextView di dalam layout
        val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
        // Menjalankan fungsi konfigurasi
        configure(alertDialogBuilder, tvDescription)

        alertDialog.show()
    }
}