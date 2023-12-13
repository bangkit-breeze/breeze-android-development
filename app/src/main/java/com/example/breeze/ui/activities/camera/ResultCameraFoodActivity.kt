package com.example.breeze.ui.activities.camera

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.databinding.ActivityResultCameraFoodBinding
import com.example.breeze.ui.viewmodel.AddFoodCarbonViewModel
import com.example.breeze.ui.activities.food.DetailResultCarbonFoodActivity
import com.example.breeze.ui.factory.TrackEmissionViewModelFactory
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.camera.reduceFileImage
import com.example.breeze.utils.camera.uriToFile
import com.example.breeze.utils.dialog.DialogUtils
import com.example.breeze.utils.dialog.ProgressDialogUtils

class ResultCameraFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultCameraFoodBinding
    private var currentImageUri: Uri? = null
    private val viewModel: AddFoodCarbonViewModel by viewModels{
        TrackEmissionViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult

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
                                    is Result.Loading -> ProgressDialogUtils.showProgressDialog(this@ResultCameraFoodActivity)
                                    is Result.Success -> {
                                        ProgressDialogUtils.hideProgressDialog()
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
                                        ProgressDialogUtils.hideProgressDialog()
                                       showFailedDialog()
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
         DialogUtils.showCustomDialog(this@ResultCameraFoodActivity, R.layout.alert_dialog_success) { alertDialogBuilder, tvDescription ->
            tvDescription.text = getString(R.string.text_image_detection)
        }
    }
    private fun showFailedDialog() {
        DialogUtils.showCustomDialogFailedWithDelay(this@ResultCameraFoodActivity,getString(R.string.text_image_detection_failed))
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