package com.chelsea.nutrilog.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelsea.nutrilog.core.api.ApiService
import com.chelsea.nutrilog.dashboard.models.DashboardSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val summary: DashboardSummary = apiService.getDashboardSummary()
                _uiState.value = DashboardUiState(summary = summary)
            } catch (e: Exception) {
                // keep default state on error
            }
        }
    }
}

data class DashboardUiState(
    val isLoading: Boolean = false,
    val summary: DashboardSummary = DashboardSummary()
)

