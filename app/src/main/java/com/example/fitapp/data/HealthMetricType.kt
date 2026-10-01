package com.example.fitapp.data

import androidx.compose.ui.graphics.Color

enum class HealthMetricType(
    val displayName: String,
    val defaultUnit: String,
    val colorHex: Long,
    val defaultDailyGoal: Double,
    val iconName: String
) {
    CALORIES("Calories", "kcal", 0xFFFF6D00, 2000.0, "local_fire_department"),
    WATER("Water", "oz", 0xFF0288D1, 64.0, "water_drop"),
    SLEEP("Sleep", "hrs", 0xFF5E35B1, 8.0, "bedtime"),
    EXERCISE("Exercise", "mins", 0xFF43A047, 30.0, "fitness_center"),
    WEIGHT("Weight", "lbs", 0xFF00897B, 150.0, "monitor_weight");

    val color: Color
        get() = Color(colorHex)

    companion object {
        fun fromString(name: String): HealthMetricType {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: CALORIES
        }
    }
}
