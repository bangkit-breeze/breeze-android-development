package com.example.breeze.ui.activities.details.carbon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.breeze.R
import com.example.breeze.databinding.ActivityDetailCarbonBinding
import com.example.breeze.databinding.ActivityMainBinding
import com.example.breeze.ui.activities.main.MainActivity

class DetailCarbonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCarbonBinding
    private var chart: AnyChartView? = null
    private val salary = listOf(12, 24)
    private val month = listOf("Food", "Vehicle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCarbonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            navigateToMainActivity()
        }
        chart = findViewById(R.id.pieChart)
        configChartView()
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