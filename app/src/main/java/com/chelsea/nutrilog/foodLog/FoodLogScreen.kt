package com.chelsea.nutrilog.foodLog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_SHORT).show()
            viewModel.clearSuccessMessage()
            viewModel.loadTodayLog() // Refresh total kalori di atas
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            TopAppBar(
                title = { Text("Add Food") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White
                )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search foods...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color(0xFF4CAF50),
                        unfocusedIndicatorColor = Color.LightGray
                    )
                )
                
                // Today's Total Card
                uiState.todayLog?.let { log ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF1F8E9)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Today's Total",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                                Text(
                                    "${log.totalCaloriesConsumed.toInt()} kcal",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    fontSize = 24.sp
                                )
                            }
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "Items: ${log.items.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                                Text(
                                    "P: ${log.totalProtein.toInt()}g | F: ${log.totalFat.toInt()}g | C: ${log.totalCarbs.toInt()}g",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF558B2F),
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
                
                // Foods Section
                Text(
                    if (searchQuery.isNotBlank()) "Search Results" else "Available Foods",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                )
                
                val foodList = if (searchQuery.isNotBlank()) 
                    uiState.searchResults 
                else 
                    uiState.foods
                
                if (foodList.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No foods found",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray
                            )
                            Text(
                                "Try searching or add a custom food",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.LightGray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                } else {
                    foodList.forEach { food ->
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
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
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
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    "P: ${food.proteinG}g | F: ${food.fatG}g | C: ${food.carbsG}g",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${food.caloriesPerServing.toInt()} kcal",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "per serving",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add food",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.padding(start = 8.dp)
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
    // Gunakan String kosong untuk mengetes TC-04-02
    var quantity by remember { mutableStateOf("") }
    // State khusus untuk menampung pesan error Test Plan
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val displayQuantity = quantity.toFloatOrNull() ?: 0f

    val caloriesTotal by remember(displayQuantity) { derivedStateOf { displayQuantity * food.caloriesPerServing } }
    val proteinTotal by remember(displayQuantity) { derivedStateOf { displayQuantity * food.proteinG } }
    val fatTotal by remember(displayQuantity) { derivedStateOf { displayQuantity * food.fatG } }
    val carbsTotal by remember(displayQuantity) { derivedStateOf { displayQuantity * food.carbsG } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Add ${food.name}", style = MaterialTheme.typography.headlineSmall)
                Divider(modifier = Modifier.padding(top = 8.dp))
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    "Portion Size (servings):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = quantity,
                    onValueChange = { newValue ->
                        quantity = newValue
                        errorMessage = null // Hapus error saat user mengetik
                    },
                    label = { Text("Quantity") },
                    singleLine = true,
                    isError = errorMessage != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (errorMessage != null) 4.dp else 16.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color(0xFF4CAF50),
                        unfocusedIndicatorColor = Color.LightGray,
                        errorContainerColor = Color(0xFFFFEBEE),
                        errorIndicatorColor = Color.Red
                    )
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                    )
                }

                // ... (Sisa kode Base Nutrition dan Total Nutrition sama persis) ...
                Text("Base Nutrition (per serving):", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                NutritionInfo(label = "Calories", value = "${food.caloriesPerServing.toInt()} kcal", color = Color(0xFF4CAF50))
                NutritionInfo(label = "Protein", value = "${food.proteinG}g", color = Color(0xFFFF7043))
                NutritionInfo(label = "Fat", value = "${food.fatG}g", color = Color(0xFFFFCA28))
                NutritionInfo(label = "Carbs", value = "${food.carbsG}g", color = Color(0xFF66BB6A))

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Text("Total for ${if (displayQuantity > 0) displayQuantity else 0} serving(s):", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                NutritionInfo(label = "Total Calories", value = "${caloriesTotal.toInt()} kcal", color = Color(0xFF4CAF50), isBold = true)
                NutritionInfo(label = "Total Protein", value = "${proteinTotal.toInt()}g", color = Color(0xFFFF7043))
                NutritionInfo(label = "Total Fat", value = "${fatTotal.toInt()}g", color = Color(0xFFFFCA28))
                NutritionInfo(label = "Total Carbs", value = "${carbsTotal.toInt()}g", color = Color(0xFF66BB6A))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // VALIDASI SESUAI TEST PLAN SAAT TOMBOL DIKLIK
                    if (quantity.isBlank()) {
                        errorMessage = "Jumlah porsi tidak boleh kosong!"
                        return@Button
                    }

                    val parsedQuantity = quantity.toFloatOrNull()
                    if (parsedQuantity == null) {
                        errorMessage = "Format angka tidak valid!"
                        return@Button
                    }
                    if (parsedQuantity < 0) {
                        errorMessage = "Jumlah porsi tidak boleh bernilai minus!"
                        return@Button
                    }
                    if (parsedQuantity == 0f) {
                        errorMessage = "Jumlah porsi harus lebih dari 0!"
                        return@Button
                    }

                    // Jika lolos semua, kirim
                    onConfirm(parsedQuantity)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Add to Today", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

@Composable
fun NutritionInfo(
    label: String,
    value: String,
    color: Color,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = color.copy(alpha = 0.1f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                label,
                style = if (isBold) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
            )
        }
        
        Text(
            value,
            style = if (isBold) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}
