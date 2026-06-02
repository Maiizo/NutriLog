package com.chelsea.nutrilog.foodLog.models

data class Food(
    val foodId: Int,
    val name: String,
    val caloriesPerServing: Double,
    val proteinG: Double,
    val fatG: Double,
    val carbsG: Double
)

data class FoodProposal(
    val proposalId: Int,
    val proposedFoodName: String,
    val proposedCalories: Double,
    val status: String, // PENDING, APPROVED, REJECTED
    val submittedBy: Int,
    val createdAt: String? = null
)

data class LogItem(
    val itemId: Int,
    val foodId: Int,
    val servingQuantity: Float,
    val consumedCalories: Double,
    val food: Food? = null
)

data class DailyLog(
    val logId: Int,
    val userId: Int,
    val date: String,
    val items: List<LogItem> = emptyList(),
    val totalCaloriesConsumed: Double = 0.0
)

data class CreateFoodLogRequest(
    val foodId: Int,
    val servingQuantity: Float,
    val date: String
)

data class ProposeFoodRequest(
    val proposedFoodName: String,
    val proposedCalories: Double
)
