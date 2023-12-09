package com.example.breeze.ui.activities.details.events

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.data.model.event.DataEvent
import com.example.breeze.databinding.ActivityDetailEventBinding

import com.example.breeze.ui.factory.EventViewModelFactory
import com.example.breeze.utils.Result
import java.text.SimpleDateFormat
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private val  viewModel: DetailEventViewModel by viewModels {
        EventViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    private lateinit var progressDialog: Dialog
    private lateinit var id: String
    private lateinit var alertDialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        viewModel.getUserLogin().observe(this@DetailEventActivity) { result ->
            result?.let {
                dataUser = it
            }
        }


        alertDialog = AlertDialog.Builder(this@DetailEventActivity)
        val storyData = intent.getParcelableExtra<DataEvent>(STORY_INTENT_DATA)

        storyData?.let {
            binding.tvPoints.text = it.rewardPoints.toString()
            binding.tvTitle.text = it.name
            binding.tvLocation.text = it.location
            binding.tvDesc.text = it.description
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val startDate = inputFormat.parse(it.startAt)
            val formattedDate = outputFormat.format(startDate)

            binding.tvDate.text = formattedDate

            id = it.id.toString()
            Glide.with(this)
                .load(it.eventImageUrl)
                .into(binding.ivEvent)


            when (storyData?.status) {
                "ACTIVE" -> {
                    binding.btnJoinEvent.visibility = View.VISIBLE
                    binding.btnUploadEvidence.visibility = View.GONE
                    binding.btnFinished.visibility = View.GONE
                    binding.tvJoined.visibility = View.GONE
                    binding.tvFinished.visibility = View.GONE
                    binding.btnJoinEvent.setOnClickListener {
                        showEventDialog()
                    }
                }
                "JOINED" -> {
                    binding.btnJoinEvent.visibility = View.GONE
                    binding.btnUploadEvidence.visibility = View.VISIBLE
                    binding.btnFinished.visibility = View.GONE
                    binding.tvJoined.visibility = View.VISIBLE
                    binding.tvFinished.visibility = View.GONE
                    binding.btnUploadEvidence.setOnClickListener {
                        startActivity(Intent(this@DetailEventActivity, FormEventActivity::class.java))
                    }
                }
                "FINISHED" -> {
                    binding.btnJoinEvent.visibility = View.GONE
                    binding.btnUploadEvidence.visibility = View.GONE
                    binding.btnFinished.visibility = View.VISIBLE
                    binding.tvJoined.visibility = View.GONE
                    binding.tvFinished.visibility = View.VISIBLE
                    binding.btnFinished.setOnClickListener {
                        Toast.makeText(this, "Anda sudah menyelesaikan event ini", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


    }






    private fun showEventDialog() {
        val customDialogView = LayoutInflater.from(this@DetailEventActivity).inflate(R.layout.alert_dialog_event, null)
        val alertDialog = buildAlertDialog(customDialogView)
        val yesButton = customDialogView.findViewById<Button>(R.id.btn_okay)
        val noButton = customDialogView.findViewById<Button>(R.id.btn_cancel)
        yesButton.setOnClickListener {
            handleYesButtonClick()
        }
        noButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    private fun buildAlertDialog(customDialogView: View): AlertDialog {
        return AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
    }
    private fun handleYesButtonClick() {
        viewModel.getUserLogin().observe(this@DetailEventActivity) { result ->
            result?.let {
                viewModel.joinEvent(it.token, id).observe(this@DetailEventActivity) { result ->
                    when (result) {
                        is Result.Loading -> showProgressDialog()
                        is Result.Success -> {
                            var storyData = intent.getParcelableExtra<DataEvent>(STORY_INTENT_DATA)

                            storyData?.let {
                                it.status = "JOINED"
                            }
                            hideProgressDialog()
                            showSuccessDialog()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this@DetailEventActivity, DetailEventActivity::class.java)
                                intent.putExtra(STORY_INTENT_DATA, storyData)
                                startActivity(intent)
                                finish()
                            }, 3000)
                        }
                        is Result.Error -> onLoginError(result.error)
                    }
                }
            }
        }



    }
    private fun showProgressDialog() {
        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.custom_progressbar)
        val progressBar: ProgressBar = progressDialog.findViewById(R.id.progressBar)
        progressBar.isIndeterminate = true
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.show()
    }
    private fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }


    private fun showSuccessDialog() {
        val customDialogView = LayoutInflater.from(this@DetailEventActivity).inflate(R.layout.alert_dialog_success, null)
        val alertDialog = android.app.AlertDialog.Builder(this@DetailEventActivity)
            .setView(customDialogView)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
        tvDescription.text = "Selamat anda telah ebrhasil bergabung dalam event ini"
        Handler(Looper.getMainLooper()).postDelayed({
            alertDialog.dismiss()
        }, 3000)
    }
    private fun onLoginError(errorMessage: String) {
        hideProgressDialog()
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}