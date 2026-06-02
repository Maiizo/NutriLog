package com.chelsea.nutrilog.core.api

import com.chelsea.nutrilog.auth.models.LoginRequest
import com.chelsea.nutrilog.auth.models.RegisterRequest
import com.chelsea.nutrilog.auth.models.AuthResponse
import com.chelsea.nutrilog.auth.models.HealthProfileRequest
import com.chelsea.nutrilog.auth.models.UserDTO
import com.chelsea.nutrilog.foodLog.models.Food
import com.chelsea.nutrilog.foodLog.models.CreateFoodLogRequest
import com.chelsea.nutrilog.foodLog.models.DailyLog
import com.chelsea.nutrilog.foodLog.models.ProposeFoodRequest
import com.chelsea.nutrilog.foodLog.models.FoodProposal
import com.chelsea.nutrilog.dashboard.models.DashboardSummary
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // ============ AUTHENTICATION ENDPOINTS ============
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
    
    @GET("auth/profile")
    suspend fun getProfile(): UserDTO
    
    @PUT("auth/profile")
    suspend fun updateProfile(@Body user: UserDTO): UserDTO

    @POST("api/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>
    // ============ FOOD ENDPOINTS ============
    @GET("food/search")
    suspend fun searchFood(@Query("query") query: String): List<Food>
    
    @GET("food/all")
    suspend fun getAllFoods(): List<Food>
    
    @GET("food/{id}")
    suspend fun getFoodById(@Path("id") id: Int): Food
    
    // ============ DAILY LOG ENDPOINTS ============
    @POST("log/add")
    suspend fun addFoodLog(@Body request: CreateFoodLogRequest): DailyLog
    
    @GET("log/today")
    suspend fun getTodayLog(): DailyLog
    
    @GET("log/{date}")
    suspend fun getLogByDate(@Path("date") date: String): DailyLog
    
    @DELETE("log/{logId}")
    suspend fun deleteLog(@Path("logId") logId: Int)

    // ============ DASHBOARD ENDPOINTS ============
    @GET("dashboard/summary")
    suspend fun getDashboardSummary(): DashboardSummary
    
    @GET("dashboard/nutrition-data")
    suspend fun getNutritionData(@Query("days") days: Int = 7): List<DailyLog>
    
    // ============ FOOD PROPOSAL ENDPOINTS ============
    @POST("food/propose")
    suspend fun proposeFoodItem(@Body request: ProposeFoodRequest): FoodProposal
    
    @GET("food/proposals")
    suspend fun getPendingProposals(): List<FoodProposal>
    
    // ============ ADMIN ENDPOINTS ============
    @GET("admin/pending-foods")
    suspend fun getPendingFoods(): List<FoodProposal>
    
    @POST("admin/approve-proposal/{proposalId}")
    suspend fun approveFoodProposal(@Path("proposalId") proposalId: Int): FoodProposal
    
    @POST("admin/reject-proposal/{proposalId}")
    suspend fun rejectFoodProposal(@Path("proposalId") proposalId: Int): FoodProposal

    @POST("api/profile/setup")
    suspend fun setupHealthProfile(@Body request: HealthProfileRequest): Response<Unit>

}
