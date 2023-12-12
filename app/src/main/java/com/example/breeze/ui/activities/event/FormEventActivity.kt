package com.example.breeze.ui.activities.event

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import com.example.breeze.utils.getImageUri
import com.example.breeze.utils.reduceFileImage
import com.example.breeze.utils.uriToFile
import com.example.breeze.utils.Result

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
        viewModel.getUserLogin().observe(this@FormEventActivity) {
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
        if ( desc.isEmpty() ) {
            when {
                desc.isEmpty() -> showToast("Descripiton harus diisi")
            }
            return
        }
        if (currentImageUri == null) {
            showToast("Please select an image")
            return
        }
        currentImageUri?.let { uri ->
            val storyData = intent.getParcelableExtra<DataEvent>(STORY_INTENT_DATA)
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edAddDescription.text.toString()
            storyData?.let {
                it.id?.let { id ->
                    viewModel.uploadEvidenceEvent(dataUser.token,
                        id, imageFile, description).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)
                                    showSuccessDialog()
                                    storyData?.let {
                                        it.status = "FINISHED"
                                    }

                                    Handler(Looper.getMainLooper()).postDelayed({
                                        val intent = Intent(this@FormEventActivity, MainActivity::class.java)
                                        // intent.putExtra(DetailEventActivity.STORY_INTENT_DATA, storyData)
                                        startActivity(intent)
                                        finish()
                                    }, 3000)
                                }

                                is Result.Error -> {
                                    showToast(result.error)
                                    showLoading(false)
                                }
                            }
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }
    private fun showSuccessDialog() {
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_success, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
        tvDescription.text = "Congratulations, you have successfully uploaded the evidence"
        handler.postDelayed({
            alertDialog.dismiss()
        }, 3000)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}