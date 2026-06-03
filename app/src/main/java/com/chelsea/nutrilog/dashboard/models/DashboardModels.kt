package com.chelsea.nutrilog.dashboard.models

import com.chelsea.nutrilog.foodLog.models.DailyLog

data class DashboardSummary(
    val todayCalories: Double,
    val targetCalories: Double,
    val bmr: Double,
    val healthProfile: HealthProfileDTO,
    val recentLogs: List<DailyLog>
)

data class HealthProfileDTO(
    val profileId: Int,
    val age: Int,
    val gender: String,
    val weightKg: Float,
    val heightCm: Float,
    val dailyTargetCalories: Double,
    val bmrResult: Double
)

data class NutritionDataPoint(
    val date: String,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)
