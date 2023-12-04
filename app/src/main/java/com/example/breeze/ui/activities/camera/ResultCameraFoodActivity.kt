package com.example.breeze.ui.activities.camera

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.example.breeze.R
import com.example.breeze.databinding.ActivityResultCameraFoodBinding

class ResultCameraFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultCameraFoodBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultCameraFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent != null) {
            currentImageUri = intent.getStringExtra(CameraFoodCarbonActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        } else {
            Log.e(TAG, "Intent is null")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    companion object {
        private const val TAG = "ResultCameraFoodActivity"
    }
}