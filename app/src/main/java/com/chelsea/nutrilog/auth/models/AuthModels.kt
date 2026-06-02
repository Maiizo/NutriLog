package com.chelsea.nutrilog.auth.models

data class LoginRequest(
    val email: String,
    val passwordHash: String // Note: Hashing should be done before sending, or handled securely via HTTPS
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class User(
    val userId: Int,
    val email: String,
    val role: String // "Admin" or "User"
)

// Added missing types expected by ApiService

data class RegisterRequest(
    val email: String,
    val passwordHash: String,
    val role: String = "User"
)

// UserDTO is used by ApiService; make it compatible with User
data class UserDTO(
    val userId: Int,
    val email: String,
    val role: String
)

data class HealthProfileRequest(
    val age: Int,
    val gender: String,
    val weightKg: Float,
    val heightCm: Float
)