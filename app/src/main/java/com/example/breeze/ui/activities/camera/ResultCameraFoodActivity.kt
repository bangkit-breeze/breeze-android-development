package com.example.breeze.ui.activities.camera

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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.example.breeze.R
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.data.model.event.DataEvent
import com.example.breeze.databinding.ActivityResultCameraFoodBinding
import com.example.breeze.ui.activities.details.events.DetailEventActivity
import com.example.breeze.ui.activities.details.events.FormEventActivity
import com.example.breeze.ui.activities.details.events.FormEventViewModel
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.activities.profile.EditPasswordActivity
import com.example.breeze.ui.factory.EventViewModelFactory
import com.example.breeze.ui.factory.TrackEmissionViewModelFactory
import com.example.breeze.utils.Result
import com.example.breeze.utils.reduceFileImage
import com.example.breeze.utils.uriToFile

class ResultCameraFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultCameraFoodBinding
    private var currentImageUri: Uri? = null
    private val viewModel: AddFoodCarbonViewModel by viewModels{
        TrackEmissionViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    private lateinit var alertDialog: AlertDialog.Builder
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultCameraFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
        binding.btnScan.setOnClickListener {
            if (currentImageUri == null) {
                showToast("Please select an image")
            }

            currentImageUri?.let { uri ->
                 val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                        viewModel.predictTrackEmissionFoodCarbon(dataUser.token, imageFile).observe(this) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> showLoading(true)
                                    is Result.Success -> {
                                        showLoading(false)
                                        val data = result.data.dataTrackFood
                                        showSuccessDialog()
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            val intent = Intent(this@ResultCameraFoodActivity, DetailResultCarbonFoodActivity::class.java)
                                            intent.putExtra(DetailResultCarbonFoodActivity.STORY_INTENT_DATA, data)
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
            } ?: showToast(getString(R.string.empty_image_warning))
        }

        viewModel.getUserLogin().observe(this@ResultCameraFoodActivity) {
            dataUser = it
        }

        val intent = intent
        if (intent != null) {
            currentImageUri = intent.getStringExtra(CameraFoodCarbonActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        } else {
            Log.e(TAG, "Intent is null")
        }
    }

    private fun showSuccessDialog() {
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_success, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
        tvDescription.text = "Gambar terdeteksi"
        handler.postDelayed({
            alertDialog.dismiss()
        }, 3000)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        private const val TAG = "ResultCameraFoodActivity"
    }

}