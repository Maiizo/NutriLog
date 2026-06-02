# NutriLog - Frontend Android App

## Project Structure

Proyek ini mengikuti **MVVM Architecture** dan **Layered Architecture** sesuai dengan proposal Software Engineering:

```
com/chelsea/nutrilog/
├── data/
│   ├── api/
│   │   ├── ApiService.kt      # Retrofit interface untuk REST API
│   │   ├── ApiClient.kt        # Retrofit setup dan interceptor
│   │   └── TokenInterceptor    # JWT token management
│   ├── models/
│   │   ├── UserModel.kt        # Data models untuk auth (User, LoginRequest, dll)
│   │   └── FoodModel.kt        # Data models untuk food (Food, DailyLog, dll)
│   └── repository/
│       └── Repositories.kt     # Repository layer (AuthRepo, FoodRepo, dll)
├── ui/
│   ├── screens/
│   │   ├── LoginScreen.kt      # UC01 - Authentication
│   │   ├── RegisterScreen.kt   # Registration
│   │   ├── DashboardScreen.kt  # UC07 - Dashboard
│   │   └── FoodLogScreen.kt    # UC04 - Log Makanan
│   ├── viewmodels/
│   │   ├── AuthViewModel.kt    # MVVM ViewModel untuk Auth
│   │   ├── FoodViewModel.kt    # MVVM ViewModel untuk Food
│   │   └── DashboardViewModel.kt # MVVM ViewModel untuk Dashboard
│   ├── components/
│   │   └── CommonComponents.kt # Reusable Composable components
│   ├── navigation/
│   │   └── Navigation.kt       # Navigation setup dengan NavController
│   └── theme/
│       ├── Theme.kt            # Material Design theme
│       ├── Color.kt            # Color palette (green theme for NutriLog)
│       └── Type.kt             # Typography setup
├── di/
│   └── ApiModule.kt            # Hilt Dependency Injection module
└── MainActivity.kt             # Entry point aplikasi
```

## Architecture Pattern

### 1. **Presentation Layer (Tier 1) - MVVM**
- **Views (UI)**: Jetpack Compose screens
- **ViewModels**: Menangani state dan business logic
- **State Management**: Menggunakan StateFlow untuk reactive UI

### 2. **Data Layer (Tier 2) - Repository Pattern**
- **Models**: Data Transfer Objects (DTOs)
- **Repository**: Interface antara API dan ViewModel
- **API Service**: Retrofit interface untuk REST API calls

### 3. **Dependency Injection**
- Menggunakan **Hilt** untuk dependency injection
- Automatic module provision untuk ApiService dan Repository

## Key Features

### ✅ Authentication (UC01)
- Login dengan email & password
- Register dengan data kesehatan (umur, berat, tinggi)
- JWT token management
- Session persistence

### ✅ Food Logging (UC04)
- Search makanan dari database
- Add food dengan serving quantity
- Track daily consumption
- Food proposal untuk makanan baru

### ✅ Dashboard (UC07)
- Display calories consumed vs target
- BMR calculation display
- Nutrition data visualization (7/14/30 days)
- Recent logs history

## Setup & Configuration

### 1. Update API Base URL
Di file `data/api/ApiClient.kt`, ubah `BASE_URL`:

```kotlin
private const val BASE_URL = "http://YOUR_SERVER_IP:8000/api/"
```

### 2. Backend Requirements
Backend harus menyediakan REST API endpoints dengan format:

```
POST   /api/auth/register
POST   /api/auth/login
GET    /api/auth/profile
GET    /api/food/search?query=
GET    /api/food/all
POST   /api/log/add
GET    /api/log/today
GET    /api/dashboard/summary
```

### 3. Token Management
Token JWT disimpan di SharedPreferences dan otomatis disertakan di setiap request via TokenInterceptor.

## UI Design

- **Color Scheme**: Green theme (#4CAF50) untuk health/nutrition concept
- **Components**: Material Design 3 dengan Jetpack Compose
- **Navigation**: Bottom navigation dengan ModalDrawer untuk menu
- **State Management**: Reactive UI dengan StateFlow

## Dependencies

```
- Jetpack Compose (UI)
- Retrofit + OkHttp (Networking)
- Hilt (Dependency Injection)
- ViewModel + StateFlow (MVVM)
- Navigation Compose (Routing)
- Gson (JSON parsing)
- Coroutines (Async operations)
```

## Next Steps

1. Setup PostgreSQL backend dengan Laravel atau Kotlin Spring Boot
2. Implement Admin Dashboard untuk validation (UC13)
3. Add Chart library (MPAndroidChart) untuk nutrition visualization
4. Implement local caching dengan Room database
5. Add unit tests untuk ViewModels dan Repositories

## Notes

- Architecture sesuai dengan design proposal
- Struktur memudahkan maintenance dan testing
- Service layer terpisah untuk business logic
- Clear separation of concerns (MVVM + Layered Architecture)
