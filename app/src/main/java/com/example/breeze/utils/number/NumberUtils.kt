package com.example.breeze.utils.number

import java.text.DecimalFormat
import kotlin.math.roundToInt

object NumberUtils {
    fun formatDecimalNumber(number: Float, decimalPlaces: Int): String {
        val pattern = buildString {
            append("#.")
            repeat(decimalPlaces) {
                append("#")
            }
        }
        val decimalFormat = DecimalFormat(pattern)
        return decimalFormat.format(number)
    }
    fun formatDecimalTwoNumber(number: Float): String {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(number)
    }
    fun calculateLevel(exp: Float): Int {
        return kotlin.math.floor(exp / 100.0).toInt()
    }
    fun calculateExpNow(exp: Float, level: Int): Float {
        return exp % (level * 100)
    }
    fun calculateExpMaxNow(level: Int): Int {
        return (level + 1) * 100
    }
    fun formatTotalCarbon(totalCo2Removed: Float?): String {
        val formattedTotalCarbon = (totalCo2Removed?.toFloat() ?: 0f) / 1000
        return formatDecimalTwoNumber(formattedTotalCarbon)
    }
    fun calculateProgress(totalCo2Removed: Float?): Int {
        val valueProgress = ((totalCo2Removed?.toFloat() ?: 0f) / 30000 * 100).toInt()
        return valueProgress
    }
    fun calculateLevelProfile(exp: Int): Int {
        return if (exp < 100) {
            1
        } else {
            Math.floor(exp / 100.0).toInt() + 1
        }
    }
    fun Float.roundToTwoDecimals(): Float {
        return (this * 100).roundToInt() / 100.0f
    }

    fun Float.roundToOneDecimal(): Float {
        return (this * 10).roundToInt() / 10.0f
    }
    fun calculateCarbonSum(value: Float?): Float? {
        return value?.div(1000)?.roundToTwoDecimals()
    }
    fun calculateCarbonPercentage(value: Float?): Float? {
        return value?.roundToOneDecimal()
    }
    fun calculateEmission(totalEmission: Int?): Double {
        return (totalEmission?.toFloat() ?: 0f) / 1000.0
    }

}