package com.example.breeze.ui.activities.details.carbon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.databinding.ActivityDetailCarbonBinding
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.factory.DetailCarbonViewModelFactory
import com.example.breeze.utils.Result
import kotlin.math.roundToInt

class DetailCarbonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCarbonBinding
    private var chart: AnyChartView? = null
    val salary = mutableListOf<Int>()
    private val month = listOf("Vehicle", "Food")
    private val viewModel: DetailCarbonViewModel by viewModels{
        DetailCarbonViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCarbonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            navigateToMainActivity()
        }

        chart = findViewById(R.id.pieChart)




    }


    override fun onResume() {
        super.onResume()
        viewModel.getUserLogin().observe(this@DetailCarbonActivity) {
            dataUser = it
        }

        viewModel.getStatistic(dataUser.token).observe(this@DetailCarbonActivity) { result ->
            when (result) {
                is Result.Loading -> return@observe
                is Result.Success -> {
                    if(result.data != null){
                        val statistic = result.data.dataUserStatistic
                        if (statistic != null) {
                            val food =  statistic.foodEmissionCount
                            val vehicle =  statistic.vehicleEmissionCount
                            if (vehicle != null) {
                                salary.add(vehicle)
                            }
                            if (food != null) {
                                salary.add(food)
                            }

                            binding.tvCountFood.text = statistic.foodEmissionCount.toString()
                            binding.tvCarbonVehicle.text = statistic.vehicleEmissionCount.toString()
                            val carbon_food_sum = statistic.foodFootprintSum?.toFloat()?.div(1000)?.roundToTwoDecimals()
                            val carbon_vehicle_sum = statistic.vehicleFootprintSum?.toFloat()?.div(1000)?.roundToTwoDecimals()
                            binding.tvCarbonFood.text = carbon_food_sum.toString()
                            binding.tvCarbonVehicle.text = carbon_vehicle_sum.toString()
                            val carbon_food_persen =  statistic.foodEmissionPercentage?.toFloat()?.roundToOneDecimal()
                            val carbon_vehicle_persen =  statistic.vehicleEmissionPercentage?.toFloat()?.roundToOneDecimal()
                             binding.tvPersenFood.text = carbon_food_persen.toString()
                             binding.tvPersenVehicle.text = carbon_vehicle_persen.toString()

                            configChartView()
                        }
                    }

                }
                is Result.Error -> {
                    val message = result.error
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun Float.roundToTwoDecimals(): Float {
        return (this * 100).roundToInt() / 100.0f
    }
    fun Float.roundToOneDecimal(): Float {
        return (this * 10).roundToInt() / 10.0f
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("return_to_home", true)
        startActivity(intent)
        finish()
    }

    private fun configChartView() {
        val pie: Pie = AnyChart.pie()
        val colors = listOf("#1BD15D", "#FBBC05")
        val dataPieChart: MutableList<DataEntry> = mutableListOf()
        for (index in salary.indices) {
            dataPieChart.add(ValueDataEntry(month.elementAt(index), salary.elementAt(index)))
        }
        pie.palette(colors.toTypedArray())
        pie.data(dataPieChart)
        pie.title("Emissions by source (kgCO2e)")
        chart!!.setChart(pie)
    }
}