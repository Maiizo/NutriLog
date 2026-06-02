# 👥 Team Briefing - NutriLog Architecture Refactoring

## 🎯 Mission
Implement NutriLog (nutrition tracking app) using **hybrid feature-based architecture** for efficient 4-person parallel development.

**Timeline**: 1 Week (7 days)
**Team Size**: 4 developers
**Architecture**: Feature-based + Shared Core
**Backend**: PostgreSQL (handled separately)

---

## 👤 Your Role Assignment

### 🔐 Person 1: Authentication Specialist
**Feature Folder**: `app/src/main/java/com/chelsea/nutrilog/auth/`

**Responsibilities**:
- User login with email/password
- User registration with health profile (age, weight, height, gender)
- Token management (save, load, clear JWT)
- Profile viewing

**Your Files**:
```
auth/
  ├── models/AuthModels.kt         ← DTOs
  ├── AuthRepository.kt            ← API calls
  ├── AuthViewModel.kt             ← State management
  ├── LoginScreen.kt               ← Login UI
  └── RegisterScreen.kt            ← Register UI
```

**Success Criteria**:
- [ ] Login works with backend
- [ ] Register saves health profile
- [ ] JWT token saved and auto-injected in headers
- [ ] Logout clears token
- [ ] Navigates to dashboard on success

---

### 🍽️ Person 2: Food Logging Specialist
**Feature Folder**: `app/src/main/java/com/chelsea/nutrilog/foodLog/`

**Responsibilities**:
- Search food items from database
- Log food to daily log with serving quantity
- Propose new foods to admin
- View today's total calories

**Your Files**:
```
foodLog/
  ├── models/FoodModels.kt         ← DTOs (Food, DailyLog, FoodProposal, etc.)
  ├── FoodRepository.kt            ← API calls
  ├── FoodService.kt               ← Validation & nutrition calc
  ├── FoodViewModel.kt             ← State management
  └── FoodLogScreen.kt             ← Food logging UI
```

**Success Criteria**:
- [ ] Search foods returns results
- [ ] Add food to log updates today's total
- [ ] Serving quantity multiplier works correctly
- [ ] Propose new food sends to backend
- [ ] Today's calories display accurate

---

### 📊 Person 3: Dashboard & Nutrition Specialist
**Feature Folder**: `app/src/main/java/com/chelsea/nutrilog/dashboard/`

**Responsibilities**:
- Display user's nutrition summary (today's calories vs target)
- Calculate BMR (Basal Metabolic Rate)
- Show nutrition trends over 7/14/30 days
- Display health profile card

**Your Files**:
```
dashboard/
  ├── models/DashboardModels.kt    ← DTOs
  ├── DashboardRepository.kt       ← API calls
  ├── NutritionService.kt          ← BMR, TDEE, progress calc
  ├── DashboardViewModel.kt        ← State management
  └── DashboardScreen.kt           ← Dashboard UI
```

**Success Criteria**:
- [ ] BMR calculation correct
- [ ] Today's calories displayed with progress bar
- [ ] History filters (7/14/30 days) work
- [ ] Nutrition data (protein, fat, carbs) show correctly
- [ ] Health profile displays age, gender, weight, height

---

### ✅ Person 4: Admin & Integration Lead
**Feature Folder**: `app/src/main/java/com/chelsea/nutrilog/admin/`

**Responsibilities**:
- View pending food proposals from users
- Approve/reject proposals
- Coordinate final integration
- Lead final testing

**Your Files**:
```
admin/
  ├── models/AdminModels.kt        ← DTOs
  ├── AdminRepository.kt           ← API calls
  ├── AdminService.kt              ← Validation logic
  ├── AdminViewModel.kt            ← State management
  └── AdminScreen.kt               ← Admin UI

navigation/
  └── Navigation.kt                ← NavHost, drawer, routing

MainActivity.kt                     ← Already updated ✅
```

**Success Criteria**:
- [ ] Pending proposals display correctly
- [ ] Approve button works with backend
- [ ] Reject button works with backend
- [ ] Drawer navigation works for all screens
- [ ] All screens navigate correctly
- [ ] Project compiles without errors

---

## 📋 Setup Instructions (All)

### 1. Initial Setup
```bash
# Clone the repo (if not already cloned)
git clone <repo-url>
cd NutriLog

# Create feature branch
git checkout -b feature/<your-feature>
# Examples:
# git checkout -b feature/auth
# git checkout -b feature/foodlog
# git checkout -b feature/dashboard
# git checkout -b feature/admin
```

### 2. Verify Project Structure
Open `AndroidStudio` → `File` → `Open` → select `NutriLog` folder

Check that you see these folders in `app/src/main/java/com/chelsea/nutrilog/`:
```
✅ core/          (Shared - DO NOT MODIFY after Day 1)
✅ auth/
✅ foodLog/
✅ dashboard/
✅ admin/
✅ components/    (Shared)
✅ navigation/    (Shared)
```

### 3. Build Project
```bash
./gradlew clean
./gradlew assembleDebug
```

If you get errors, check:
- [ ] All imports are correct (use IDE autocomplete)
- [ ] @HiltViewModel, @Inject annotations present
- [ ] Package names match file locations

### 4. Implement Your Feature
- Use existing `*ViewModel.kt` as template for state management
- Use existing `*Repository.kt` as template for API calls
- Use `components/CommonComponents.kt` for UI (buttons, text fields, loading)
- Follow MVVM pattern: UI → ViewModel → Repository → ApiService

---

## 🔌 API Integration Reference

All API calls go through `core/api/ApiService.kt`:

```kotlin
// Example: In FoodRepository.kt
suspend fun searchFood(query: String): Result<List<Food>> = try {
    Result.success(apiService.searchFood(query))  // ← Calls ApiService
} catch (e: Exception) {
    Result.failure(e)
}
```

**Backend Base URL**: `http://192.168.1.1:8000/api/` (change in `ApiClient.kt`)

**JWT Token**: Automatically added to all requests via `TokenInterceptor`

---

## 📱 UI Component Library

Use these reusable components from `components/CommonComponents.kt`:

```kotlin
// Button
NutriLogButton(
    text = "Login",
    onClick = { /* ... */ },
    isLoading = uiState.isLoading
)

// Text Field
NutriLogTextField(
    value = email,
    onValueChange = { email = it },
    label = "Email",
    keyboardType = KeyboardType.Email
)

// Loading Overlay
LoadingIndicator()  // Shows centered spinner

// Error Message
ErrorMessage(uiState.error)  // Shows error card

// Color Reference
Color(0xFF4CAF50)  // Primary Green
Color(0xFF2E7D32)  // Dark Green
```

---

## 🚀 Development Workflow (Days 2-6)

### Every Day
1. Pull latest from `main` branch
2. Work on your feature folder
3. Commit to your feature branch (e.g., `feature/auth`)
4. Test your features

### Each Friday
1. Create Pull Request from your branch → `main`
2. Wait for approval
3. Merge to `main`

### Merge Command
```bash
git checkout main
git pull origin main
git merge feature/<your-feature>
git push origin main
```

---

## ⚠️ Important Rules

### ❌ DO NOT
- Modify `core/` folder after Day 1 (use it as-is)
- Modify other team members' feature folders
- Add external dependencies without team approval
- Commit directly to `main` branch

### ✅ DO
- Keep your feature folder independent
- Use the shared `components/` and `navigation/`
- Ask questions in team chat
- Test before committing
- Write clear commit messages

---

## 📞 Getting Help

| Issue | Solution |
|-------|----------|
| "Cannot resolve symbol ApiService" | Check `core/api/ApiService.kt` exists |
| "Hilt DI errors" | Ensure @Inject on constructor, @HiltViewModel on VM |
| "@AndroidEntryPoint on MainActivity not working" | Add `hilt_android` to build.gradle.kts |
| "Build errors - unresolved imports" | Run `./gradlew clean` then rebuild |
| "Navigation not working" | Verify `navigation/Navigation.kt` is imported in MainActivity |

---

## 📊 Progress Tracking

**Week Breakdown**:
- **Day 1**: Setup + verify compilation
- **Days 2-4**: Core feature development
- **Days 5-6**: Testing + bug fixes
- **Day 7**: Integration + final testing

**Check Status**:
- Each feature branch should be mergeable by Day 6
- All tests passing by Day 7
- Ready for QA testing by end of Day 7

---

## 🎓 Reference Documentation

- `ARCHITECTURE.md` - Complete structure guide
- `REFACTORING_COMPLETE.md` - File inventory and status
- Each file has JavaDoc comments explaining classes/functions

---

**Questions?** Ask in team chat or at daily standup 💬

**Ready?** Start with `git checkout -b feature/<your-feature>` now! 🚀
