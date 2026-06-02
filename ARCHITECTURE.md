# NutriLog - Feature-Based Architecture (Refactored)

## 📁 Struktur Folder Baru

```
app/src/main/java/com/chelsea/nutrilog/
├── core/                    ← SHARED (Read-only after Day 1)
│   ├── api/
│   │   ├── ApiService.kt    (All 24+ endpoints)
│   │   └── ApiClient.kt     (Retrofit setup + TokenInterceptor)
│   ├── di/
│   │   └── ApiModule.kt     (Hilt @Module)
│   └── theme/
│       ├── Color.kt         (Green color palette)
│       └── Theme.kt         (Material Design 3 config)
│
├── auth/                    ← PERSON 1 (Independent)
│   ├── models/
│   │   └── AuthModels.kt    (LoginRequest, RegisterRequest, AuthResponse, UserDTO)
│   ├── AuthRepository.kt    (API calls + token management)
│   ├── AuthViewModel.kt     (State management, MVVM)
│   ├── LoginScreen.kt       (UI Screen)
│   └── RegisterScreen.kt    (UI Screen)
│
├── foodLog/                 ← PERSON 2 (Independent)
│   ├── models/
│   │   └── FoodModels.kt    (Food, DailyLog, LogItem, FoodProposal, etc.)
│   ├── FoodRepository.kt    (API calls)
│   ├── FoodService.kt       (Business logic: validation, formatting)
│   ├── FoodViewModel.kt     (State management, MVVM)
│   └── FoodLogScreen.kt     (UI Screen + Dialog)
│
├── dashboard/               ← PERSON 3 (Independent)
│   ├── models/
│   │   └── DashboardModels.kt (DashboardSummary, HealthProfileDTO, NutritionDataPoint)
│   ├── DashboardRepository.kt  (API calls)
│   ├── NutritionService.kt     (Business logic: BMR calc, TDEE, progress)
│   ├── DashboardViewModel.kt   (State management, MVVM)
│   └── DashboardScreen.kt      (UI Screen)
│
├── admin/                   ← PERSON 4 (Optional Advanced)
│   ├── models/
│   │   └── AdminModels.kt   (AdminDashboardState)
│   ├── AdminRepository.kt   (API calls for admin endpoints)
│   ├── AdminService.kt      (Business logic: validation)
│   ├── AdminViewModel.kt    (State management, MVVM)
│   └── AdminScreen.kt       (UI Screen)
│
├── components/              ← SHARED (Used by all features)
│   └── CommonComponents.kt  (NutriLogButton, NutriLogTextField, LoadingIndicator, etc.)
│
├── navigation/              ← SHARED (Central coordination)
│   └── Navigation.kt        (NavHost, DrawerContent, route definitions)
│
└── MainActivity.kt          ← Entry point (updated imports)
```

## 👥 Person Assignment

| Person | Feature | Main Files | Responsibilities |
|--------|---------|-----------|------------------|
| 1 | auth/ | AuthViewModel, LoginScreen, RegisterScreen, AuthRepository | User authentication, registration, profile |
| 2 | foodLog/ | FoodViewModel, FoodLogScreen, FoodRepository, FoodService | Food search, logging, proposals |
| 3 | dashboard/ | DashboardViewModel, DashboardScreen, DashboardRepository, NutritionService | Nutrition tracking, BMR calc, trends |
| 4 | admin/ + integration | AdminViewModel, AdminScreen, Navigation.kt, MainActivity | Food validation UI, navigation, final build |

## 🔑 Key Changes from Layer-Based

### Old Structure (Layer-based - merge conflicts)
```
data/
  api/          ← ALL teams touch this!
  models/       ← ALL teams touch this!
  repository/   ← ALL teams touch this!
ui/
  screens/      ← ALL teams touch this!
  viewmodels/   ← ALL teams touch this!
  components/
  navigation/
```

### New Structure (Feature-based - minimal conflicts)
```
auth/      ← Person 1 ONLY
foodLog/   ← Person 2 ONLY
dashboard/ ← Person 3 ONLY
admin/     ← Person 4 ONLY
core/      ← Everyone uses, but read-only after Day 1
components/← Shared, but changes agreed in team
navigation/← Shared, but centralized
```

## 📝 Development Strategy

### Day 1 - Setup (All together)
- ✅ **Core folder created** - ApiService, ApiModule, Theme
- Verify compilation
- Verify DI works

### Day 2-6 - Parallel Development (Split teams)
- **Person 1**: Implement auth/ features
  - Test login/register with backend
  - Save token to SharedPreferences
  
- **Person 2**: Implement foodLog/ features
  - Test food search, add to log
  - Test food proposals
  
- **Person 3**: Implement dashboard/ features
  - Display user's nutrition summary
  - Show trends over 7/14/30 days
  
- **Person 4**: Implement admin/ features
  - Display pending food proposals
  - Approve/reject proposals

### Day 7 - Integration & Testing (All together)
- Merge all branches
- Test all flows end-to-end
- Fix any integration issues
- Deploy

## 🔄 Merge Strategy

Each person works in feature branch:
```bash
# Day 1 - Create branches
git checkout -b feature/auth
git checkout -b feature/foodlog
git checkout -b feature/dashboard
git checkout -b feature/admin

# Days 2-6 - Work independently
# Each person pushes to their branch

# Day 7 - Merge to main
git checkout main
git merge feature/auth      # No conflicts (only auth/ touched)
git merge feature/foodlog   # No conflicts (only foodLog/ touched)
git merge feature/dashboard # No conflicts (only dashboard/ touched)
git merge feature/admin     # No conflicts (only admin/ touched)
```

## ✅ Verification Checklist

### Compilation
- [ ] Project compiles without errors
- [ ] All imports resolve correctly
- [ ] Hilt DI compiles successfully

### Runtime
- [ ] MainActivity launches without crashing
- [ ] Navigation works (drawer, routes)
- [ ] Auth flow works (login → dashboard)
- [ ] Each feature operates independently

## 📚 Dependencies Between Features

```
auth/          ← No dependencies on other features
  ↓
foodLog/       ← Depends on: core.api, components
  ↓
dashboard/     ← Depends on: core.api, components
  ↓
admin/         ← Depends on: core.api, foodLog.models, components
  ↓
navigation/    ← Depends on: ALL screens
  ↓
MainActivity   ← Depends on: navigation, core.theme
```

## 🛠️ Build & Run

```bash
# Build project
./gradlew assembleDebug

# Run on device
./gradlew installDebug

# Run tests
./gradlew test
```

## 📞 Common Issues & Fixes

**Issue**: "Package com.chelsea.nutrilog.auth not found"
→ **Fix**: Check that auth/AuthModels.kt exists in correct package

**Issue**: "Cannot resolve symbol ApiService"
→ **Fix**: Verify core/api/ApiService.kt is created

**Issue**: "Hilt DI error in MainActivity"
→ **Fix**: Ensure @AndroidEntryPoint is on MainActivity

**Issue**: "Navigation not working"
→ **Fix**: Verify navigation/Navigation.kt has all screens and drawer content

---

**Last Updated**: After refactoring to feature-based architecture
**Architecture Type**: Hybrid Feature-Based + Shared Core
**Team Size**: 4 people
**Estimated Duration**: 1 week
