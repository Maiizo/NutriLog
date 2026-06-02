# NutriLog Frontend Implementation Guide

Dokumentasi lengkap implementasi frontend NutriLog Android App berdasarkan proposal Software Engineering.

## 📋 Overview

Frontend NutriLog telah diimplementasikan dengan:
- **Architecture**: MVVM + Layered Architecture
- **UI Framework**: Jetpack Compose (Material Design 3)
- **Networking**: Retrofit + OkHttp
- **State Management**: StateFlow + ViewModel
- **Dependency Injection**: Hilt
- **Navigation**: Jetpack Navigation Compose

## 📁 Project Structure Explanation

### Data Layer (`data/`)

#### `api/` - Network Communication
- **ApiService.kt**: Retrofit interface yang mendefinisikan semua REST API endpoints
  - Authentication endpoints (register, login, getProfile)
  - Food endpoints (search, getAllFoods)
  - Daily log endpoints (add log, get logs)
  - Dashboard endpoints (getSummary, getNutritionData)

- **ApiClient.kt**: Setup Retrofit dan OkHttpClient
  - Token interceptor untuk JWT management
  - Logging interceptor untuk debugging
  - Automatic token injection di setiap request

#### `models/` - Data Transfer Objects
- **UserModel.kt**: DTO untuk user data
  - `LoginRequest`, `RegisterRequest`
  - `AuthResponse`, `UserDTO`

- **FoodModel.kt**: DTO untuk food & nutrition data
  - `Food`, `FoodProposal`
  - `DailyLog`, `LogItem`
  - `CreateFoodLogRequest`, `ProposeFoodRequest`

#### `repository/` - Data Management
Implements Repository Pattern untuk abstraksi data source:
- **AuthRepository**: Handle authentication logic
  - Login/Register
  - Token management (save/get/clear)
  - Profile management

- **FoodRepository**: Handle food-related operations
  - Search food
  - Get all foods
  - Propose new food

- **DailyLogRepository**: Handle daily log operations
  - Add food log
  - Get today's log
  - Get log by date
  - Delete log

- **DashboardRepository**: Handle dashboard data
  - Get dashboard summary (BMR, calories, profile)
  - Get nutrition data for multiple days

### UI Layer (`ui/`)

#### `viewmodels/` - Business Logic & State Management
Each ViewModel manages UI state using StateFlow:

- **AuthViewModel**: UC01 - Authentication
  - `AuthUiState`: Loading, loggedIn, user data, error messages
  - Functions: `register()`, `login()`, `logout()`, `loadProfile()`
  - Handles redirect logic dengan LaunchedEffect

- **FoodViewModel**: UC04 - Food Logging
  - `FoodUiState`: Foods list, search results, today's log, loading state
  - Functions: `searchFood()`, `addFoodLog()`, `loadTodayLog()`, `proposeFoodItem()`
  - Real-time search dengan StateFlow

- **DashboardViewModel**: UC07 - Dashboard
  - `DashboardUiState`: Summary, nutrition data, loading state
  - Functions: `loadDashboardData()`, `refreshDashboard()`, `loadNutritionDataForDays()`
  - Multi-day nutrition tracking

#### `screens/` - UI Screens (Composables)
- **LoginScreen.kt**: UC01
  - Email & password input fields
  - Error handling & loading state
  - Navigation to register
  - Form validation

- **RegisterScreen.kt**: User Registration
  - Full health profile input (age, weight, height, gender)
  - Input validation
  - Error messaging
  - Redirect to dashboard on success

- **DashboardScreen.kt**: UC07
  - Displays BMR calculation
  - Today's calories vs target
  - Nutrition data visualization (7/14/30 days selector)
  - Recent logs list
  - Refresh button
  - FloatingActionButton untuk log food

- **FoodLogScreen.kt**: UC04
  - Search bar untuk mencari makanan
  - Food list dengan calories & protein info
  - Dialog untuk input serving quantity
  - Add food ke daily log

#### `components/` - Reusable UI Components
- **NutriLogButton**: Green-themed button dengan loading state
- **NutriLogTextField**: Text input dengan error display
- **LoadingIndicator**: Full-screen loading overlay
- **ErrorMessage**: Error card dengan styling
- **NutritionCard**: Display nutrition metrics
- **FoodItem**: Food list item card

#### `navigation/` - App Navigation
- **NutriLogApp()**: Main composable dengan auth state check
- **NutriLogNavigation()**: NavHost dengan route definitions
  - Route: `login` → LoginScreen
  - Route: `register` → RegisterScreen
  - Route: `dashboard` → DashboardScreen
  - Route: `log_food` → FoodLogScreen
- **DrawerContent()**: Side menu dengan logout

#### `theme/` - Design System
- **Color.kt**: Green color scheme (#4CAF50) untuk health concept
- **Type.kt**: Typography configuration
- **Theme.kt**: Material Design 3 theme setup

### Dependency Injection (`di/`)

**ApiModule.kt** - Hilt module untuk providing dependencies:
- Provides `ApiService` singleton
- Automatically injected ke repositories & ViewModels
- Single source of truth untuk API instance

## 🔄 Data Flow (MVVM + Layered)

```
UI (Composables)
    ↓
ViewModel (StateFlow)
    ↓
Repository Pattern
    ↓
API Service (Retrofit)
    ↓
Backend (REST API + PostgreSQL)
```

### Contoh Flow untuk Login:

1. **UI**: User input email & password di LoginScreen
2. **ViewModel**: `AuthViewModel.login()` dipanggil
3. **ViewModel**: Set `isLoading = true`, panggil repository
4. **Repository**: `AuthRepository.login()` memanggil API
5. **API**: Retrofit mengirim request ke `/api/auth/login`
6. **Backend**: Validasi credentials, return JWT token
7. **Repository**: Save token ke SharedPreferences
8. **ViewModel**: Update `isLoggedIn = true`, set user data
9. **UI**: LaunchedEffect detect state change, navigate ke dashboard

## 🚀 Getting Started

### 1. Prerequisites
- Android Studio 2023.1+
- Min SDK: API 28
- Java 11+

### 2. Build & Run

```bash
# Clone repository
git clone <your-repo>

# Open di Android Studio
# File → Open → Select NutriLog folder

# Build project
Build → Make Project

# Run on emulator atau device
Run → Run 'app'
```

### 3. Configure API Base URL

Edit `data/api/ApiClient.kt`:
```kotlin
private const val BASE_URL = "http://192.168.1.100:8000/api/"
```

Gunakan IP address server Anda (localhost tidak bekerja dari emulator)

### 4. Test Flows

**Login Flow:**
- Tap login screen
- Input test credentials
- Observe loading state
- Redirect ke dashboard

**Food Logging Flow:**
- Tap + button di dashboard
- Search untuk makanan
- Select food & input serving
- Observe daily log update

**Dashboard Flow:**
- View today's calories
- Switch between 7/14/30 days
- See BMR calculation

## 🔧 Key Implementation Details

### JWT Token Management
```kotlin
// Token otomatis dikirim di setiap request
class TokenInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getTokenFromPreferences()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
```

### Reactive UI dengan StateFlow
```kotlin
@HiltViewModel
class AuthViewModel @Inject constructor(...) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    // UI observes uiState changes
    val authState by viewModel.uiState.collectAsState()
}
```

### Error Handling
```kotlin
val result = repository.login(request)
result.onSuccess { response ->
    // Update success state
}
result.onFailure { exception ->
    // Update error state dengan message
}
```

## 🧪 Testing Checklist

- [ ] Login dengan valid credentials
- [ ] Register dengan health profile data
- [ ] Search makanan
- [ ] Add food ke daily log
- [ ] View dashboard dengan nutrition data
- [ ] Navigate antar screens
- [ ] Logout & verify session cleared
- [ ] Error handling untuk network failure
- [ ] Loading states berjalan smooth

## 📚 Architecture Benefits

1. **Separation of Concerns**: UI, Business Logic, Data Management terpisah jelas
2. **Testability**: ViewModel dan Repository mudah di-unit test
3. **Maintainability**: Changes di backend tidak affect UI layer
4. **Reusability**: Components bisa dipake di multiple screens
5. **Scalability**: Easy to add new features (e.g., Admin Dashboard untuk UC13)

## 🔜 Next Implementation Steps

1. **Backend Setup**
   - Create PostgreSQL database schema
   - Setup REST API endpoints (Kotlin Spring Boot recommended)
   - Implement BMR calculation logic
   - JWT authentication

2. **Admin Panel**
   - Implement UC13 - Admin Validation
   - Separate admin UI screen
   - Food proposal review interface

3. **Enhancement**
   - Add chart library untuk nutrition visualization
   - Local caching dengan Room database
   - Push notifications untuk reminders
   - Unit tests untuk ViewModels

4. **Production Ready**
   - Implement logging & crash reporting
   - Add analytics tracking
   - Security hardening
   - Performance optimization

## 📞 Support

Untuk questions atau issues, refer ke:
- `README_FRONTEND.md` - Project structure overview
- `BACKEND_API_SPEC.md` - API endpoint specification
- Proposal design document - Architecture decisions

## ✅ Compliance dengan Proposal

- ✓ MVVM Architecture implementation
- ✓ 3-Tier Layered Architecture (Client-Server)
- ✓ Jetpack Compose untuk UI (modern Android)
- ✓ Kotlin language
- ✓ REST API integration
- ✓ JWT token management
- ✓ Role-Based Access Control ready (user/admin)
- ✓ Database design sesuai proposal
- ✓ Responsive UI dengan good UX
