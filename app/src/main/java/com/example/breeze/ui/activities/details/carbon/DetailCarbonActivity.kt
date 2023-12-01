package com.example.breeze.ui.activities.details.carbon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.breeze.R

class DetailCarbonActivity : AppCompatActivity() {
    private var chart: AnyChartView? = null
    private val salary = listOf(12, 24)
    private val month = listOf("Food", "Vehicle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_carbon)

        chart = findViewById(R.id.pieChart)

        configChartView()
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