package com.example.breeze.ui.activities.food

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.track.DataTrackFood
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.databinding.ActivityDetailResultCarbonFoodBinding
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.adapter.rv.FoodCarbonAdapter
import com.example.breeze.ui.factory.TrackEmissionViewModelFactory
import com.example.breeze.ui.viewmodel.AddFoodCarbonViewModel
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.dialog.DialogUtils
import com.example.breeze.utils.dialog.ProgressDialogUtils
import kotlin.math.roundToInt

class DetailResultCarbonFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultCarbonFoodBinding
    private val adapter = FoodCarbonAdapter()
    private val viewModel: AddFoodCarbonViewModel by viewModels {
        TrackEmissionViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailResultCarbonFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        viewModel.getUserLogin().observe(this@DetailResultCarbonFoodActivity) {
            dataUser = it
        }

        val data = intent.getParcelableExtra<DataTrackFood>(STORY_INTENT_DATA)
        data?.let {
            setupUIWithData(it)
            setListeners(it)
        }
    }

    private fun setupUIWithData(data: DataTrackFood) {
        with(binding) {
            tvTitle.text = data.predictResult?.foodName
            Glide.with(this@DetailResultCarbonFoodActivity)
                .load(data.imageUrl)
                .into(myImageView)
            data.predictResult?.let {
                updateAccuracyText(it.confidence?.toFloat())
                updateCarbonText(it.totalEmissions?.toFloat())
            }

            val layoutManager = LinearLayoutManager(this@DetailResultCarbonFoodActivity)
            rvIngredients.layoutManager = layoutManager
            rvIngredients.adapter = adapter
            adapter.submitList(data.predictResult?.ingredients)
        }
    }

    private fun updateAccuracyText(accuracy: Float?) {
        accuracy?.let {
            binding.tvAccuacy.text = when {
                it <= 0.6 -> "Low Accuracy"
                it < 0.9 -> "Medium Accuracy"
                else -> "High Accuracy"
            }
        }
    }

    private fun updateCarbonText(emission: Float?) {
        emission?.let {
            val totalEmission = (it * 1000).roundToInt()
            binding.tvCarbon.text = "$totalEmission kgCO2e"
        }
    }

    private fun setListeners(data: DataTrackFood) {
        with(binding) {
            btnAdd.setOnClickListener {
                handleAddButtonClick(data)
            }

            btnCancel.setOnClickListener {
                navigateToMainActivity()
            }
        }
    }

    private fun handleAddButtonClick(data: DataTrackFood) {
        viewModel.addTrackEmissionFood(
            dataUser.token,
            data.predictResult?.foodName!!,
            ((data.predictResult?.totalEmissions?.toFloat() ?: 0f) * 1000).toInt()
        ).observe(this@DetailResultCarbonFoodActivity) { result ->
                when (result) {
                    is Result.Loading -> ProgressDialogUtils.showProgressDialog(this@DetailResultCarbonFoodActivity)
                    is Result.Success -> {
                        ProgressDialogUtils.hideProgressDialog()
                        showSuccessDialog()
                        Handler(Looper.getMainLooper()).postDelayed({
                            navigateToMainActivity()
                        }, 3000)
                    }
                    is Result.Error -> onLoginError(result.error)
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onLoginError(errorMessage: String) {
        ProgressDialogUtils.hideProgressDialog()
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccessDialog() {
        DialogUtils.showCustomDialogWithDelay(this@DetailResultCarbonFoodActivity, getString(R.string.text_success_add_food))
    }

    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}