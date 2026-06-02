package com.chelsea.nutrilog.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dashboard") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Total kcal today: ${uiState.summary.totalCaloriesToday.toInt()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Average kcal: ${uiState.summary.averageCaloriesPerDay.toInt()}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.refresh() }) {
                Text("Refresh")
            }
        }
    }
}

