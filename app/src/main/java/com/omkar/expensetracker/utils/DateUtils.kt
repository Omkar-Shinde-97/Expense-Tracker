package com.omkar.expensetracker.utils

import android.util.Log
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {

    private val YYYY_MM_DD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    fun getCurrentDateTime(): String {
        val now = ZonedDateTime.now()
        return now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    fun extractDateOnly(): String {
        val zonedDateTime =
            ZonedDateTime.parse(getCurrentDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        return zonedDateTime.format(formatter)
    }

    fun getPastNDaysDateStrings(days: Int): List<String> {
        val dates = mutableListOf<String>()
        val todayUtc = LocalDate.now(java.time.ZoneOffset.UTC)
        for (i in 0 until days) {
            dates.add(todayUtc.minusDays(i.toLong()).format(YYYY_MM_DD_FORMATTER))
        }
        return dates.reversed()
    }

    fun getDayOfWeekAbbreviation(dateString: String): String {
        return try {
            val date = LocalDate.parse(dateString, YYYY_MM_DD_FORMATTER)
            date.dayOfWeek.getDisplayName(/* style = */ TextStyle.SHORT, /* locale = */
                Locale.ENGLISH
            )
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            "Error"
        }
    }
}