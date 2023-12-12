package com.example.breeze.ui.activities.carbon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.user.HistoryTrackResponse
import com.example.breeze.databinding.ActivityDetailCarbonBinding
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.adapter.rv.HistoryTrackAdapter
import com.example.breeze.ui.factory.DetailCarbonViewModelFactory
import com.example.breeze.ui.viewmodel.DetailCarbonViewModel
import com.example.breeze.utils.constans.Result
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
    private val adapter = HistoryTrackAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCarbonBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.topAppBar.setNavigationOnClickListener {
            navigateToMainActivity()
        }

        chart = findViewById(R.id.pieChart)

        setupRecyclerView(adapter)


    }

    private fun setupRecyclerView(adapter: HistoryTrackAdapter) {
        val layoutManager = LinearLayoutManager(this@DetailCarbonActivity)
        binding.rvActivity.layoutManager = layoutManager
        binding.rvActivity.adapter = adapter
    }
    private fun handleEventResult(result: Result<HistoryTrackResponse>, adapter: HistoryTrackAdapter) {
        when (result) {
            is Result.Loading -> {
                binding.shimmerView.startShimmerAnimation()
            }
            is Result.Success -> {
                binding.shimmerView.stopShimmerAnimation()
                binding.shimmerView.visibility = View.GONE
                binding.dataMainLayout.visibility = View.VISIBLE
                val data = result.data.dataHistoryTrack
                if(data.isNullOrEmpty()){
                    binding.activityDontHave.visibility = View.VISIBLE
                    binding.rvActivity.visibility = View.GONE
                }else{
                    binding.activityDontHave.visibility = View.GONE
                    binding.rvActivity.visibility = View.VISIBLE
                    adapter.submitList(data)
                }
            }
            is Result.Error -> {
                binding.shimmerView.stopShimmerAnimation()
                binding.shimmerView.visibility = View.GONE
                binding.dataMainLayout.visibility = View.VISIBLE
                val message = result.error
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        binding.dataMainLayout.visibility = View.GONE
        binding.shimmerView.visibility = View.VISIBLE
        binding.shimmerView.startShimmerAnimation()
        viewModel.getUserLogin().observe(this@DetailCarbonActivity) {
            dataUser = it
        }
        viewModel.getHistoryTrack(dataUser.token).observe(this) { result ->
            handleEventResult(result, adapter)
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
                            binding.tvCountVehicle.text = statistic.vehicleEmissionCount.toString()
                            val carbon_food_sum = statistic.foodFootprintSum?.toFloat()?.div(1000)?.roundToTwoDecimals()
                            val carbon_vehicle_sum = statistic.vehicleFootprintSum?.toFloat()?.div(1000)?.roundToTwoDecimals()
                            binding.tvCarbonFood.text = carbon_food_sum.toString()
                            binding.tvCarbonVehicle.text = carbon_vehicle_sum.toString()
                            val carbon_food_persen =  statistic.foodEmissionPercentage?.toFloat()?.roundToOneDecimal()
                            val carbon_vehicle_persen =  statistic.vehicleEmissionPercentage?.toFloat()?.roundToOneDecimal()
                             binding.tvPersenFood.text = carbon_food_persen.toString()
                             binding.tvPersenVehicle.text = carbon_vehicle_persen.toString()


                            if(food == 0 && vehicle == 0 &&
                                carbon_food_sum?.toInt() == 0 && carbon_vehicle_sum?.toInt() == 0 &&
                                carbon_food_persen?.toInt() == 0 && carbon_vehicle_persen?.toInt() == 0)
                            {
                                binding.carbonDontHave.visibility = View.VISIBLE
                                binding.pieChart.visibility = View.GONE
                            }else{
                                binding.carbonDontHave.visibility = View.GONE
                                binding.pieChart.visibility = View.VISIBLE
                                configChartView()
                            }

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