package com.example.breeze.utils.dialog

import android.app.Dialog
import android.content.Context
import android.widget.ProgressBar
import com.example.breeze.R

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
}