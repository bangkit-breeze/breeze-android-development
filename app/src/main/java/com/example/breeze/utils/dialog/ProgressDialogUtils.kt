package com.example.breeze.utils.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.breeze.R
import com.google.android.material.button.MaterialButton

object ProgressDialogUtils {
    private lateinit var progressDialog: Dialog

    fun showProgressDialog(context: Context) {
        progressDialog = Dialog(context)
        progressDialog.setContentView(R.layout.custom_progressbar)
        val progressBar: ProgressBar = progressDialog.findViewById(R.id.progressBar)
        progressBar.isIndeterminate = true
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.show()
    }

    fun hideProgressDialog() {
        if (ProgressDialogUtils::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    fun showProgressBarDialog(
        context: Context,
        totalRemoved: Int,
        valueProgress: Int
    ) {
        val customDialogView =
            LayoutInflater.from(context).inflate(R.layout.alert_dialog_carbon, null)
        val alertDialog = AlertDialog.Builder(context)
            .setView(customDialogView)
            .create()

        val progressCarbon = customDialogView.findViewById<TextView>(R.id.tv_progress)
        val progressBarCarbon = customDialogView.findViewById<ProgressBar>(R.id.progressBar_circular)
        val ivClose = customDialogView.findViewById<ImageView>(R.id.iv_close)
        val btnOkay = customDialogView.findViewById<MaterialButton>(R.id.btn_okay)

        progressBarCarbon.progress = totalRemoved
        progressCarbon.text = "${valueProgress}%"

        ivClose.setOnClickListener {
            alertDialog.dismiss()
        }

        btnOkay.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }
}