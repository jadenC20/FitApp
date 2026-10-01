package com.example.fitapp.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HealthEntry(
    val id: Long = 0,
    val metricType: HealthMetricType,
    val value: Double,
    val unit: String = metricType.defaultUnit,
    val notes: String = "",
    val dateMillis: Long = System.currentTimeMillis(),
    val photoUri: String? = null
) {
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
            return sdf.format(Date(dateMillis))
        }

    val formattedShortDate: String
        get() {
            val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
            return sdf.format(Date(dateMillis))
        }

    val formattedValue: String
        get() {
            return if (value % 1.0 == 0.0) {
                "${value.toInt()} $unit"
            } else {
                "${String.format(Locale.getDefault(), "%.1f", value)} $unit"
            }
        }
}
