package com.example.breeze.utils.number

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val outputFormatWithMonthName = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    fun formatDate(inputDate: String): String {
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
    fun formatDateWithMonthName(inputDate: String): String {
        val date = inputFormat.parse(inputDate)
        return outputFormatWithMonthName.format(date)
    }

    fun calculateDaysDifference(startAt: String): String {
        val date = inputFormat.parse(startAt)
        val currentDate = Date()
        val diffInMillies = date.time - currentDate.time
        val diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

        val dayDifference = Math.abs(diffInDays)
        return if (diffInDays > 0) {
            "$dayDifference days left"
        } else if (diffInDays < 0) {
            "$dayDifference days ago"
        } else {
            "Today"
        }
    }
}