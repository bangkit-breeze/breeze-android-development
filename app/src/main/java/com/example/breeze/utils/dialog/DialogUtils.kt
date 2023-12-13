package com.example.breeze.utils.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
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
        val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
        configure(alertDialogBuilder, tvDescription)
        alertDialog.show()
    }

    fun showCustomDialogWithDelay(context: Context, description: String) {
        showCustomDialog(context, R.layout.alert_dialog_success) { builder, textView ->
            textView.text = description
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                builder.create().dismiss()
            }, 3000)
        }
    }

    fun showCustomDialogFailedWithDelay(context: Context, description: String) {
        showCustomDialog(context, R.layout.alert_dialog_failed) { builder, textView ->
            textView.text = description
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                builder.create().dismiss()
            }, 3000)
        }
    }


}