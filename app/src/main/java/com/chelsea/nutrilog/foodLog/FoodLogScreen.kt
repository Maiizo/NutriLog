package com.chelsea.nutrilog.foodLog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chelsea.nutrilog.components.LoadingIndicator
import com.chelsea.nutrilog.components.NutriLogTextField
import com.chelsea.nutrilog.foodLog.models.Food
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodLogScreen(
    viewModel: FoodViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.searchFood(searchQuery)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            TopAppBar(
                title = { Text("Log Food") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White
                )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Search Bar
                NutriLogTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = "Search foods...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                
                // Today's Total
                uiState.todayLog?.let { log ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF1F8E9)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Today's Total",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray
                            )
                            Text(
                                "${log.totalCaloriesConsumed.toInt()} kcal",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
                
                // Food List
                Text(
                    if (searchQuery.isNotBlank()) "Search Results" else "All Foods",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val foodList = if (searchQuery.isNotBlank()) 
                        uiState.searchResults 
                    else 
                        uiState.foods
                    
                    items(foodList) { food ->
                        FoodItemCard(
                            food = food,
                            onClick = {
                                selectedFood = food
                                showAddDialog = true
                            }
                        )
                    }
                }
            }
        }
        
        // Loading Overlay
        if (uiState.isLoading) {
            LoadingIndicator()
        }
    }
    
    // Add Food Dialog
    if (showAddDialog && selectedFood != null) {
        AddFoodDialog(
            food = selectedFood!!,
            onConfirm = { quantity ->
                viewModel.addFoodLog(selectedFood!!.foodId, quantity)
                showAddDialog = false
                selectedFood = null
            },
            onDismiss = {
                showAddDialog = false
                selectedFood = null
            }
        )
    }
}

@Composable
fun FoodItemCard(
    food: Food,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    food.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "P: ${food.proteinG}g | F: ${food.fatG}g | C: ${food.carbsG}g",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            
            Text(
                "${food.caloriesPerServing.toInt()} kcal",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AddFoodDialog(
    food: Food,
    onConfirm: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    var quantity by remember { mutableStateOf("1") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add ${food.name}") },
        text = {
            Column {
                Text("Serving quantity (multiplier):", style = MaterialTheme.typography.bodyMedium)
                TextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Text(
                    "Total: ${(quantity.toFloatOrNull() ?: 1f) * food.caloriesPerServing.toInt()} kcal",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color(0xFF4CAF50)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(quantity.toFloatOrNull() ?: 1f)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
