package com.chelsea.nutrilog.dashboard.models

// Minimal dashboard summary model used by ApiService
data class DashboardSummary(
    val totalCaloriesToday: Double = 0.0,
    val averageCaloriesPerDay: Double = 0.0
)

// Health profile DTO placeholder
data class HealthProfileDTO(
    val age: Int = 0,
    val gender: String = "",
    val weightKg: Float = 0f,
    val heightCm: Float = 0f
)

