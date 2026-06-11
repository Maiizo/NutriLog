package com.chelsea.nutrilog.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chelsea.nutrilog.auth.AuthViewModel
import com.chelsea.nutrilog.auth.LoginScreen
import com.chelsea.nutrilog.auth.RegisterScreen
import com.chelsea.nutrilog.dashboard.DashboardScreen
import com.chelsea.nutrilog.dashboard.DashboardViewModel
import com.chelsea.nutrilog.foodLog.FoodLogScreen
import com.chelsea.nutrilog.foodLog.FoodViewModel
import com.chelsea.nutrilog.admin.AdminScreen
import com.chelsea.nutrilog.admin.AdminViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutriLogApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsState()

    // Logika Pintar: Jika sudah login, langsung tampilkan App utama.
    // Jika belum, tampilkan halaman Login/Register.
    if (authState.isLoggedIn) {
        AppWithNavigation(navController, authViewModel)
    } else {
        AuthNavigation(navController, authViewModel)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AuthNavigation(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    // Kosongkan saja.
                    // Karena state uiState.isLoggedIn sudah berubah menjadi true,
                    // file NutriLogApp() akan mendeteksinya dan OTOMATIS
                    // memindahkan halaman ke Dashboard.
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            // Memanggil RegisterScreen yang sudah memiliki input Age, Weight, Height
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    // Kosongkan saja: Jika sukses, authState.isLoggedIn menjadi true,
                    // dan NutriLogApp() akan otomatis pindah ke Dashboard
                },
                onNavigateBack = {
                    navController.popBackStack() // Kembali ke halaman Login
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppWithNavigation(navController: NavHostController, authViewModel: AuthViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authState by authViewModel.uiState.collectAsState()
    val userRole = authState.user?.role ?: "user"
    val isAdmin = userRole == "admin"

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    isAdmin = isAdmin,
                    onDashboard = {
                        navController.navigate("dashboard")
                        scope.launch { drawerState.close() }
                    },
                    onLogFood = {
                        navController.navigate("log_food")
                        scope.launch { drawerState.close() }
                    },
                    onAdmin = {
                        navController.navigate("admin")
                        scope.launch { drawerState.close() }
                    },
                    onLogout = {
                        authViewModel.logout()
                        scope.launch { drawerState.close() }
                    }
                )
            }
        },
        scrimColor = Color.Black.copy(alpha = 0.32f),
        gesturesEnabled = true,
        drawerState = drawerState
    ) {
        NavHost(navController, startDestination = "dashboard") {
            composable("dashboard") {
                val viewModel: DashboardViewModel = hiltViewModel()
                DashboardScreenWithDrawer(viewModel = viewModel, drawerState = drawerState, scope = scope)
            }

            composable("log_food") {
                val viewModel: FoodViewModel = hiltViewModel()
                FoodLogScreenWithDrawer(viewModel = viewModel, drawerState = drawerState, scope = scope)
            }

            if (isAdmin) {
                composable("admin") {
                    val viewModel: AdminViewModel = hiltViewModel()
                    AdminScreenWithDrawer(viewModel = viewModel, drawerState = drawerState, scope = scope)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DashboardScreenWithDrawer(
    viewModel: DashboardViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Dashboard") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Open Menu")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF4CAF50),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )
        DashboardScreen(viewModel = viewModel)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FoodLogScreenWithDrawer(
    viewModel: FoodViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Log Food") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Open Menu")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF4CAF50),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )
        FoodLogScreen(viewModel = viewModel)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AdminScreenWithDrawer(
    viewModel: AdminViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Admin Panel") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Open Menu")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF4CAF50),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )
        AdminScreen(viewModel = viewModel)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DrawerContent(
    isAdmin: Boolean = false,
    onDashboard: () -> Unit,
    onLogFood: () -> Unit,
    onAdmin: (() -> Unit)? = null,
    onLogout: () -> Unit
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        NavigationDrawerItem(
            label = { Text("Dashboard") },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            selected = false,
            onClick = onDashboard,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color(0xFF4CAF50).copy(alpha = 0.12f),
                unselectedTextColor = Color.Black
            )
        )

        NavigationDrawerItem(
            label = { Text("Log Food") },
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Log Food") },
            selected = false,
            onClick = onLogFood,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color(0xFF4CAF50).copy(alpha = 0.12f),
                unselectedTextColor = Color.Black
            )
        )

        if (isAdmin && onAdmin != null) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            NavigationDrawerItem(
                label = { Text("Admin Panel") },
                icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin Panel") },
                selected = false,
                onClick = onAdmin,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFF4CAF50).copy(alpha = 0.12f),
                    unselectedTextColor = Color(0xFF2E7D32)
                )
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        NavigationDrawerItem(
            label = { Text("Logout") },
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout") },
            selected = false,
            onClick = onLogout,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color.Red.copy(alpha = 0.12f),
                unselectedTextColor = Color.Red
            )
        )
    }
}