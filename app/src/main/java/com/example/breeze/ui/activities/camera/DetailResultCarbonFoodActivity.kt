package com.example.breeze.ui.activities.camera

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
import com.example.breeze.ui.adapter.FoodCarbonAdapter
import com.example.breeze.ui.factory.TrackEmissionViewModelFactory
import com.example.breeze.utils.Result
import kotlin.math.roundToInt

class DetailResultCarbonFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultCarbonFoodBinding
    private val adapter = FoodCarbonAdapter()
    private val  viewModel: AddFoodCarbonViewModel by viewModels {
        TrackEmissionViewModelFactory.getInstance(application)
    }
    private lateinit var progressDialog: Dialog
    private lateinit var dataUser: LoginResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailResultCarbonFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getUserLogin().observe(this@DetailResultCarbonFoodActivity) {
            dataUser = it
        }

        val data = intent.getParcelableExtra<DataTrackFood>(STORY_INTENT_DATA)
        if (data != null) {
            val foodName = data.predictResult?.foodName
            binding.tvTitle.text = foodName
            Glide.with(this)
                .load(data.imageUrl)
                .into(binding.myImageView)
            val acuracy = data.predictResult?.confidence?.toFloat()
            if (acuracy != null) {
                if(acuracy <= 0.6 ){
                    binding.tvAccuacy.text = "Low Accuracy"
                }else if(acuracy < 0.9 && acuracy >= 0.61){
                    binding.tvAccuacy.text = "Medium Accuracy"
                }else if(acuracy > 0.9){
                    binding.tvAccuacy.text = "High Accuracy"
                }
            }

            val emision = data.predictResult?.totalEmissions?.toFloat()
            val totalEmision = emision?.let { (it * 1000).roundToInt() } ?: 0
            binding.tvCarbon.text = "${data.predictResult?.totalEmissions} kgCO2e"
            val layoutManager = LinearLayoutManager(this)
            binding.rvIngredients.layoutManager = layoutManager
            binding.rvIngredients.adapter = adapter
            adapter.submitList(data.predictResult?.ingredients)
            binding.btnAdd.setOnClickListener {
                viewModel.addTrackEmissionFood(dataUser.token, foodName!!, totalEmision).observe(this@DetailResultCarbonFoodActivity) { result ->
                    when (result) {
                        is Result.Loading -> showProgressDialog()
                        is Result.Success -> {
                            hideProgressDialog()
                            showSuccessDialog()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 3000)
                        }
                        is Result.Error -> onLoginError(result.error)
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
    private fun onLoginError(errorMessage: String) {
        hideProgressDialog()
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
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
        Handler(Looper.getMainLooper()).postDelayed({
            alertDialog.dismiss()
        }, 3000)
    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }

}