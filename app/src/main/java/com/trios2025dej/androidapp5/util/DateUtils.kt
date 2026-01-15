package com.trios2025dej.androidapp5.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

object DateUtils {
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    private val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    fun jsonDateToShortDate(date: String): String {
        return try {
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            ""
        }
    }

    fun xmlDateToDate(dateString: String?): Date {
        val date = dateString ?: return Date()
        val inFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault())
        return inFormat.parse(date) ?: Date()
    }

    fun dateToShortDate(date: java.util.Date?): String {
        if (date == null) return ""
        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        return sdf.format(date)
    }

    fun durationTextToMillis(durationText: String?): Long? {
        if (durationText.isNullOrBlank()) return null

        // Common RSS formats:
        // "1234" (seconds)
        // "12:34" (mm:ss)
        // "1:02:03" (hh:mm:ss)
        val parts = durationText.trim().split(":")
        return try {
            val seconds = when (parts.size) {
                1 -> parts[0].toLong()
                2 -> parts[0].toLong() * 60 + parts[1].toLong()
                3 -> parts[0].toLong() * 3600 + parts[1].toLong() * 60 + parts[2].toLong()
                else -> return null
            }
            seconds * 1000
        } catch (_: Exception) {
            null
        }
    }

}