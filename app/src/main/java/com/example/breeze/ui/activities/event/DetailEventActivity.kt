package com.example.breeze.ui.activities.event

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.event.DataEvent
import com.example.breeze.databinding.ActivityDetailEventBinding
import com.example.breeze.ui.viewmodel.DetailEventViewModel
import com.example.breeze.ui.factory.EventViewModelFactory
import com.example.breeze.utils.number.DateUtils
import com.example.breeze.utils.SnackbarUtils
import com.example.breeze.utils.constans.Constants
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.dialog.DialogUtils
import com.example.breeze.utils.dialog.ProgressDialogUtils

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private val  viewModel: DetailEventViewModel by viewModels {
        EventViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    private lateinit var id: String
    private lateinit var alertDialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        setupUI()
    }
    private fun setupUI() {
        setupUserDataObserver()
        alertDialog = AlertDialog.Builder(this@DetailEventActivity)
        val storyData = intent.getParcelableExtra<DataEvent>(STORY_INTENT_DATA)
        storyData?.let {
            setupEventDetails(it)
        }
    }
    private fun setupUserDataObserver() {
        viewModel.getSession().observe(this@DetailEventActivity) { result ->
            result?.let {
                dataUser = it
            }
        }
    }
    private fun setupEventDetails(storyData: DataEvent) {
        with(binding) {
            tvPoints.text = storyData.rewardPoints.toString()
            tvTitle.text = storyData.name
            tvLocation.text = storyData.location
            tvDesc.text = storyData.description
            tvDate.text = storyData.startAt?.let {
                DateUtils.formatDateWithMonthName(it)
            }
            id = storyData.id.toString()
            Glide.with(this@DetailEventActivity)
                .load(storyData.eventImageUrl)
                .into(ivEvent)
        }
        handleEventStatus(storyData)
    }
    private fun handleEventStatus(storyData: DataEvent) {
        with(binding) {
            when (storyData.status) {
                "ACTIVE" -> {
                    btnJoinEvent.visibility = View.VISIBLE
                    btnJoinEvent.setOnClickListener {
                        showEventDialog()
                    }
                    btnUploadEvidence.visibility = View.GONE
                    btnFinished.visibility = View.GONE
                }
                "JOINED" -> {
                    btnUploadEvidence.visibility = View.VISIBLE
                    tvJoined.visibility = View.VISIBLE
                    btnUploadEvidence.setOnClickListener {
                        startActivity(Intent(this@DetailEventActivity, FormEventActivity::class.java).apply {
                            putExtra(STORY_INTENT_DATA, storyData)
                        })
                        finish()
                    }
                    btnJoinEvent.visibility = View.GONE
                    btnFinished.visibility = View.GONE
                }
                "FINISHED" -> {
                    btnFinished.visibility = View.VISIBLE
                    tvFinished.visibility = View.VISIBLE
                    btnFinished.setOnClickListener {
                        SnackbarUtils.showWithDismissAction(binding.root,  R.string.text_event_finished)
                    }
                    btnJoinEvent.visibility = View.GONE
                    btnUploadEvidence.visibility = View.GONE
                }
                else -> return
            }
        }
    }
    private fun showEventDialog() {
        val customDialogView = LayoutInflater.from(this@DetailEventActivity).inflate(R.layout.alert_dialog_event, null)
        val alertDialog = buildAlertDialog(customDialogView)
        customDialogView.findViewById<Button>(R.id.btn_okay).setOnClickListener {
            alertDialog.dismiss()
            handleYesButtonClick(alertDialog)
        }
        customDialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }
    private fun buildAlertDialog(customDialogView: View): AlertDialog =
        AlertDialog.Builder(this).setView(customDialogView).create()
    private fun handleYesButtonClick(alertDialog: AlertDialog) {
        viewModel.getSession().observe(this@DetailEventActivity) { result ->
            result?.let {
                viewModel.joinEvent(it.token, id).observe(this@DetailEventActivity) { result ->
                    when (result) {
                        is Result.Loading -> ProgressDialogUtils.showProgressDialog(this@DetailEventActivity)
                        is Result.Success -> handleJoinEventSuccess()
                        is Result.Error -> onLoginError(result.error)
                    }
                }
            }
        }
    }
    private fun handleJoinEventSuccess() {
        var storyData = intent.getParcelableExtra<DataEvent>(STORY_INTENT_DATA)
        storyData?.status = "JOINED"
        ProgressDialogUtils.hideProgressDialog()
        showSuccessDialog()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@DetailEventActivity, DetailEventActivity::class.java).putExtra(STORY_INTENT_DATA, storyData))
            finish()
        }, Constants.DIALOG_DELAY)
    }
    private fun showSuccessDialog() {
        DialogUtils.showCustomDialog(this@DetailEventActivity, R.layout.alert_dialog_success) { _, tvDescription ->
            tvDescription.text = getString(R.string.text_event_joined_done)
        }
    }
    private fun onLoginError(errorMessage: String) {
        ProgressDialogUtils.hideProgressDialog()
        SnackbarUtils.showWithDismissAction(binding.root, errorMessage)
    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}