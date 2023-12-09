package com.example.breeze.ui.activities.details.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.event.DataEvent
import com.example.breeze.databinding.ActivityDetailEventBinding
import com.example.breeze.ui.activities.login.LoginActivity
import com.example.breeze.ui.activities.register.RegisterActivity
import java.text.SimpleDateFormat
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
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


            Glide.with(this)
                .load(it.eventImageUrl)
                .into(binding.ivEvent)


            when (storyData?.status) {
                "ACTIVE" -> {
                    binding.btnJoinEvent.visibility = View.VISIBLE
                    binding.btnUploadEvidence.visibility = View.GONE
                    binding.btnFinished.visibility = View.GONE
                    binding.btnJoinEvent.setOnClickListener {
                        showEventDialog()
                    }
                }
                "JOINED" -> {
                    binding.btnJoinEvent.visibility = View.GONE
                    binding.btnUploadEvidence.visibility = View.VISIBLE
                    binding.btnFinished.visibility = View.GONE
                    binding.btnUploadEvidence.setOnClickListener {
                        startActivity(Intent(this@DetailEventActivity, FormEventActivity::class.java))
                    }
                }
                "FINISHED" -> {
                    binding.btnJoinEvent.visibility = View.GONE
                    binding.btnUploadEvidence.visibility = View.GONE
                    binding.btnFinished.visibility = View.VISIBLE
                    binding.btnFinished.setOnClickListener {
                        Toast.makeText(this, "Anda sudah menyelesaikan event ini", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


    }


    private fun showEventDialog() {
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_event, null)
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

    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}