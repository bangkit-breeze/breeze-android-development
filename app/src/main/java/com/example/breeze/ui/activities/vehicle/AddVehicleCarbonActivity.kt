package com.example.breeze.ui.activities.vehicle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.breeze.R
import com.example.breeze.databinding.ActivityAddVehicleCarbonBinding
import com.example.breeze.databinding.ActivityLoginBinding
import kotlin.math.roundToInt

class AddVehicleCarbonActivity : AppCompatActivity() {
    private var selectedItemId: Int = -1
        private lateinit var binding: ActivityAddVehicleCarbonBinding
    private var lastSelectedTextView: TextView? = null
    private var selectedVehicle: String? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityAddVehicleCarbonBinding.inflate(layoutInflater)
            setContentView(binding.root)



            selectedVehicle = "DefaultText"
            binding.item1.setOnClickListener { handleItemClick(binding.item1, binding.tvVehicle1, "Car") }
            binding.item2.setOnClickListener { handleItemClick(binding.item2, binding.tvVehicle2, "Bus") }
            binding.item3.setOnClickListener { handleItemClick(binding.item3, binding.tvVehicle3, "Car") }
            binding.item4.setOnClickListener { handleItemClick(binding.item4, binding.tvVehicle4, "Bus") }
            binding.item5.setOnClickListener { handleItemClick(binding.item5, binding.tvVehicle5, "Car") }
            binding.item6.setOnClickListener { handleItemClick(binding.item6, binding.tvVehicle6, "Bus") }
            binding.coba.text = selectedVehicle


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
                            binding.etMileage.setSelection(binding.etMileage.length())  // Move cursor to the end
                        }
                        binding.mainSlider.value = enteredValue.toFloat()
                    }
                }
            })
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
                imageView.setImageResource(R.drawable.ic_vehicle_active)
            }
            R.id.item4 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_4)
                imageView.setImageResource(R.drawable.ic_bus_active)
            }
            R.id.item5 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_5)
                imageView.setImageResource(R.drawable.ic_vehicle_active)
            }
            R.id.item6 -> {
            val imageView = findViewById<ImageView>(R.id.ic_vehicle_6)
            imageView.setImageResource(R.drawable.ic_bus_active)
        }
        }
        selectedVehicle = vehicleType
        binding.coba.text = selectedVehicle
    }



    private fun resetItemSelection(item: LinearLayout, textView: TextView?) {
        item.isSelected = false
        item.setBackgroundResource(R.drawable.bg_vehicle)
        textView?.setTextColor(resources.getColor(R.color.black))

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
                imageView.setImageResource(R.drawable.ic_vehicle_nonactive)
            }
            R.id.item4 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_4)
                imageView.setImageResource(R.drawable.ic_bus_nonactive)
            }
            R.id.item5 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_5)
                imageView.setImageResource(R.drawable.ic_vehicle_nonactive)
            }
            R.id.item6 -> {
                val imageView = findViewById<ImageView>(R.id.ic_vehicle_6)
                imageView.setImageResource(R.drawable.ic_bus_nonactive)
            }

        }

        selectedItemId = -1
        lastSelectedTextView = null
    }

}