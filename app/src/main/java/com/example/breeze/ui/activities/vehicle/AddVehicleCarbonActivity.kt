package com.example.breeze.ui.activities.vehicle

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.databinding.ActivityAddVehicleCarbonBinding
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.factory.TrackEmissionViewModelFactory
import com.example.breeze.ui.viewmodel.AddVehicleCarbonViewModel
import com.example.breeze.utils.constans.Result
import kotlin.math.roundToInt

class AddVehicleCarbonActivity : AppCompatActivity() {
    private var selectedItemId: Int = -1
    private lateinit var binding: ActivityAddVehicleCarbonBinding
    private var lastSelectedTextView: TextView? = null
    private var selectedVehicle: String? = null
    private val  viewModel: AddVehicleCarbonViewModel by viewModels {
        TrackEmissionViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    private lateinit var progressDialog: Dialog
    private lateinit var alertDialog: AlertDialog.Builder
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityAddVehicleCarbonBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }

            viewModel.getUserLogin().observe(this@AddVehicleCarbonActivity) { result ->
                result?.let {
                    dataUser = it
                }
            }

            alertDialog = AlertDialog.Builder(this@AddVehicleCarbonActivity)

            // selectedVehicle = "car"
            binding.item1.setOnClickListener { handleItemClick(binding.item1, binding.tvVehicle1, "car") }
            binding.item2.setOnClickListener { handleItemClick(binding.item2, binding.tvVehicle2, "bus") }
            binding.item3.setOnClickListener { handleItemClick(binding.item3, binding.tvVehicle3, "bike") }
            binding.item4.setOnClickListener { handleItemClick(binding.item4, binding.tvVehicle4, "ferry") }
            binding.item5.setOnClickListener { handleItemClick(binding.item5, binding.tvVehicle5, "plane") }
            binding.item6.setOnClickListener { handleItemClick(binding.item6, binding.tvVehicle6, "train") }
            // binding.coba.text = selectedVehicle




            binding.mainSlider.setLabelFormatter { value ->
                "${value.toInt()} KM"
            }
            binding.mainSlider.addOnChangeListener { slider, value, fromUser ->
                slider.value = value.roundToInt().toFloat()
                binding.etMileage.setText(value.toInt().toString())
            }



            binding.etMileage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {

                    val enteredValue = s.toString().toDoubleOrNull() ?: 0.0
                    if (enteredValue > 200) {
                        binding.etMileage.setText("200")
                    }else {
                        if (binding.etMileage.isFocused) {
                            binding.etMileage.setSelection(binding.etMileage.length())
                        }
                        binding.mainSlider.value = enteredValue.toFloat()
                    }
                }
            })

            binding.btnAdd.setOnClickListener {
                val mileageValue = binding.etMileage.text.toString()
                if (mileageValue.isEmpty()) {
                    showToast("Tolong isi jarak kendaraan!")
                }
                if (selectedVehicle?.isNotEmpty() == true) {
                    viewModel.addTrackEmissionVehicleCarbon(dataUser.token, selectedVehicle!!, mileageValue.toInt()).observe(this@AddVehicleCarbonActivity) { result ->
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
                } else {
                    showToast("Please select a vehicle first!")
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
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_success, null)
        val alertDialog = android.app.AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
        tvDescription.text = "Anda telah menambahkan track vehcle carbon, anda dapat 10xp"
        Handler(Looper.getMainLooper()).postDelayed({
            alertDialog.dismiss()
        }, 3000)
    }
    private fun onLoginError(errorMessage: String) {
        hideProgressDialog()
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleItemClick(item: LinearLayout, textView: TextView, vehicleType: String) {
        if (selectedItemId == item.id) {
            selectedVehicle = null
            return
        }

        if (selectedItemId != -1) {
            resetItemSelection(findViewById(selectedItemId), lastSelectedTextView)
        }

        selectedItemId = item.id
        lastSelectedTextView = textView

        item.isSelected = true
        item.setBackgroundResource(R.drawable.bg_vehicle_active)
        textView.setTextColor(resources.getColor(R.color.light_primary))
        when (item.id) {
            R.id.item1 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_1)
                imageView.setImageResource(R.drawable.ic_vehicle_active)
            }
            R.id.item2 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_2)
                imageView.setImageResource(R.drawable.ic_bus_active)
            }
            R.id.item3 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_3)
                imageView.setImageResource(R.drawable.ic_bike_active)
            }
            R.id.item4 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_4)
                imageView.setImageResource(R.drawable.ic_train_active)
            }
            R.id.item5 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_5)
                imageView.setImageResource(R.drawable.ic_plane_active)
            }
            R.id.item6 -> {
            val imageView = findViewById<ImageView>(R.id.ic_vehicle_6)
            imageView.setImageResource(R.drawable.ic_ferry_active)
        }
        }
        selectedVehicle = vehicleType
        binding.coba.text = selectedVehicle
    }



    private fun resetItemSelection(item: LinearLayout, textView: TextView?) {
        item.isSelected = false
        item.setBackgroundResource(R.drawable.bg_vehicle)
        textView?.setTextColor(0xFF858585.toInt())

        when (item.id) {
            R.id.item1 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_1)
                imageView.setImageResource(R.drawable.ic_vehicle_nonactive)
            }
            R.id.item2 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_2)
                imageView.setImageResource(R.drawable.ic_bus_nonactive)
            }
            R.id.item3 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_3)
                imageView.setImageResource(R.drawable.ic_bike_nonactive)
            }
            R.id.item4 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_4)
                imageView.setImageResource(R.drawable.ic_train_nonactive)
            }
            R.id.item5 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_5)
                imageView.setImageResource(R.drawable.ic_plane_nonactive)
            }
            R.id.item6 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_6)
                imageView.setImageResource(R.drawable.ic_ferry_nonactive)
            }

        }

        selectedItemId = -1
        lastSelectedTextView = null
    }

}