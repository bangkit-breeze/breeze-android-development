package com.example.breeze.ui.activities.event

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.event.DataEvent
import com.example.breeze.databinding.ActivityFormEventBinding
import com.example.breeze.ui.viewmodel.FormEventViewModel
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.factory.EventViewModelFactory
import com.example.breeze.utils.camera.getImageUri
import com.example.breeze.utils.camera.reduceFileImage
import com.example.breeze.utils.camera.uriToFile
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.dialog.DialogUtils
import com.example.breeze.utils.showToastString
import com.example.breeze.utils.showLoading

class FormEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormEventBinding
    private var currentImageUri: Uri? = null
    private val viewModel: FormEventViewModel by viewModels{
        EventViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    private lateinit var alertDialog: AlertDialog.Builder
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getSession().observe(this@FormEventActivity) {
            dataUser = it
        }
        alertDialog = AlertDialog.Builder(this@FormEventActivity)
        binding.btnGalerry.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
        binding.btnCamera.setOnClickListener { startCamera() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        val desc = binding.edAddDescription.text.toString().trim()
        if (desc.isEmpty()) {
            showToast(getString(R.string.empty_desc_warning))
            return
        }

        currentImageUri?.let { uri ->
            val storyData = intent.getParcelableExtra<DataEvent>(STORY_INTENT_DATA)
            val imageFile = uriToFile(uri, this).reduceFileImage()

            viewModel.uploadEvidenceEvent(
                dataUser.token,
                storyData?.id ?: return,
                imageFile,
                desc
            ).observe(this) { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> handleUploadSuccess(storyData)
                    is Result.Error -> handleUploadError(result.error)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun handleUploadSuccess(storyData: DataEvent?) {
        showLoading(false)
        showSuccessDialog()
        storyData?.status = "FINISHED"
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@FormEventActivity, MainActivity::class.java))
            finish()
        }, 3000)
    }

    private fun handleUploadError(errorMessage: String) {
        showToast(errorMessage)
        showLoading(false)
    }

    private fun showSuccessDialog() {
        DialogUtils.showCustomDialog(this@FormEventActivity, R.layout.alert_dialog_success) { _, tvDescription ->
            tvDescription.text = getString(R.string.text_event_evidence_done)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.let { showLoading(it, isLoading) }
    }

    private fun showToast(message: String) {
        showToastString(this, message)
    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}