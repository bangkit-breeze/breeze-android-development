package com.example.breeze.ui.activities.carbon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.breeze.data.model.response.user.DataHistoryTrack
import com.example.breeze.data.model.response.user.DataUserStatistic
import com.example.breeze.data.model.response.user.HistoryTrackResponse
import com.example.breeze.databinding.ActivityDetailCarbonBinding
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.adapter.rv.HistoryTrackAdapter
import com.example.breeze.ui.factory.DetailCarbonViewModelFactory
import com.example.breeze.ui.viewmodel.DetailCarbonViewModel
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.number.NumberUtils.calculateCarbonPercentage
import com.example.breeze.utils.number.NumberUtils.calculateCarbonSum
import com.example.breeze.utils.number.NumberUtils.roundToOneDecimal
import com.example.breeze.utils.number.NumberUtils.roundToTwoDecimals
import com.example.breeze.utils.showToastString

class DetailCarbonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCarbonBinding
    private var chart: AnyChartView? = null
    val dataCarbon = mutableListOf<Int>()
    private val category = listOf("Vehicle", "Food")
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
    private fun setupLoadingUI() {
        binding.dataMainLayout.visibility = View.GONE
        binding.shimmerView.visibility = View.VISIBLE
        binding.shimmerView.startShimmerAnimation()
    }
    private fun stopLoadingUI() {
        binding.shimmerView.stopShimmerAnimation()
        binding.shimmerView.visibility = View.GONE
        binding.dataMainLayout.visibility = View.VISIBLE
    }
    private fun observeUserData() {
        viewModel.getUserLogin().observe(this@DetailCarbonActivity) {
            dataUser = it
        }
    }

    private fun observeHistoryTrack() {
        viewModel.getHistoryTrack(dataUser.token).observe(this) { result ->
            handleTrackHistoryResult(result, adapter)
        }
    }
    private fun setupRecyclerView(adapter: HistoryTrackAdapter) {
        val layoutManager = LinearLayoutManager(this@DetailCarbonActivity)
        binding.rvActivity.layoutManager = layoutManager
        binding.rvActivity.adapter = adapter
    }
    private fun handleTrackHistoryResult(result: Result<HistoryTrackResponse>, adapter: HistoryTrackAdapter) {
        binding.shimmerView.startShimmerAnimation()

        when (result) {
            is Result.Loading ->  binding.shimmerView.startShimmerAnimation()
            is Result.Success -> handleSuccessHistoryResult(result.data.dataHistoryTrack, adapter)
            is Result.Error -> handleFailureHistoryResult(result.error)
        }
    }

    private fun handleSuccessHistoryResult(data: List<DataHistoryTrack?>?, adapter: HistoryTrackAdapter) {
        stopLoadingUI()
        with(binding) {
            activityDontHave.visibility = if (data.isNullOrEmpty()) View.VISIBLE else View.GONE
            rvActivity.visibility = if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
        data?.takeIf { it.isNotEmpty() }?.let {
            adapter.submitList(it)
        }
    }
    private fun handleFailureHistoryResult(errorMessage: String) {
        stopLoadingUI()
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        setupLoadingUI()
        observeUserData()
        observeHistoryTrack()
        observeStatistic()
    }
    private fun observeStatistic() {
        viewModel.getStatistic(dataUser.token).observe(this@DetailCarbonActivity) { result ->
            when (result) {
                is Result.Loading -> return@observe
                is Result.Success -> handleSuccessStatistic(result.data?.dataUserStatistic)
                is Result.Error -> showError(result.error)
            }
        }
    }
    private fun handleSuccessStatistic(statistic: DataUserStatistic?) {
        statistic?.let { stats ->
            val food = stats.foodEmissionCount
            val vehicle = stats.vehicleEmissionCount
            updateDataCarbon(vehicle, food)
            with(binding) {
                tvCountFood.text = food?.toString()
                tvCountVehicle.text = vehicle?.toString()
                val carbonFoodSum = calculateCarbonSum(stats.foodFootprintSum?.toFloat())
                val carbonVehicleSum = calculateCarbonSum(stats.vehicleFootprintSum?.toFloat())
                tvCarbonFood.text = carbonFoodSum.toString()
                tvCarbonVehicle.text = carbonVehicleSum.toString()
                val carbonFoodPercentage = calculateCarbonPercentage(stats.foodEmissionPercentage?.toFloat())
                val carbonVehiclePercentage = calculateCarbonPercentage(stats.vehicleEmissionPercentage?.toFloat())
                tvPersenFood.text = carbonFoodPercentage.toString()
                tvPersenVehicle.text = carbonVehiclePercentage.toString()
                val isDataZero = (food == 0 && vehicle == 0 &&
                        carbonFoodSum?.toInt() == 0 && carbonVehicleSum?.toInt() == 0 &&
                        carbonFoodPercentage?.toInt() == 0 && carbonVehiclePercentage?.toInt() == 0)

                updateViewsVisibility(isDataZero)
                if (!isDataZero) {
                    configChartView()
                }
            }
        }
    }
    private fun updateDataCarbon(vehicle: Int?, food: Int?) {
        dataCarbon.addAll(listOfNotNull(vehicle, food))
    }
    private fun updateViewsVisibility(isDataZero: Boolean) {
        with(binding) {
            carbonDontHave.visibility = if (isDataZero) View.VISIBLE else View.GONE
            pieChart.visibility = if (isDataZero) View.GONE else View.VISIBLE
        }
    }
    private fun showError(errorMessage: String) {
        showToastString(this, errorMessage)
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
        for (index in dataCarbon.indices) {
            dataPieChart.add(ValueDataEntry(category.elementAt(index), dataCarbon.elementAt(index)))
        }
        pie.palette(colors.toTypedArray())
        pie.data(dataPieChart)
        pie.title("Emissions by source (kgCO2e)")
        chart!!.setChart(pie)
    }
}