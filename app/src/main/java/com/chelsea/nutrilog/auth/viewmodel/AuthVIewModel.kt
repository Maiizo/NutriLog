package com.chelsea.nutrilog.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelsea.nutrilog.auth.models.LoginRequest
import com.chelsea.nutrilog.auth.models.RegisterRequest
import com.chelsea.nutrilog.core.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.chelsea.nutrilog.auth.models.AuthResponse

@HiltViewModel
class AuthViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, passwordHash: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val request = LoginRequest(email, passwordHash)
                // Use the simpler login API that returns AuthResponse directly
                val response: AuthResponse = apiService.login(request)
                val userRole = response.user.role
                _authState.value = AuthState.Success(userRole)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Network error: ${e.message}")
            }
        }
    }

    fun register(email: String, passwordHash: String, role: String = "User") {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val request = RegisterRequest(email, passwordHash, role)
                val response: AuthResponse = apiService.register(request)
                _authState.value = AuthState.Success(response.user.role)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val role: String) : AuthState()
    data class Error(val message: String) : AuthState()
}