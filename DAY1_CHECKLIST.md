# 📋 Day 1 Setup Checklist

## Team Lead Tasks (Person 4 - Admin/Integration)

- [ ] Verify all 27 files are created in correct locations
- [ ] Run `./gradlew clean && ./gradlew assembleDebug`
- [ ] Fix any compilation errors
- [ ] Share project with other team members
- [ ] Create git feature branches for each person:
  ```bash
  git checkout -b feature/auth
  git checkout -b feature/foodlog
  git checkout -b feature/dashboard
  git checkout -b feature/admin
  ```
- [ ] Assign each person their branch
- [ ] Schedule daily standup (10 min per day)

## Person 1: Auth Setup

- [ ] Check out `feature/auth` branch
- [ ] Import project in AndroidStudio
- [ ] Verify these files exist:
  - [ ] `auth/models/AuthModels.kt`
  - [ ] `auth/AuthRepository.kt`
  - [ ] `auth/AuthViewModel.kt`
  - [ ] `auth/LoginScreen.kt`
  - [ ] `auth/RegisterScreen.kt`
- [ ] Run project and verify it launches
- [ ] Navigate to login screen
- [ ] Test that UI components render correctly

## Person 2: FoodLog Setup

- [ ] Check out `feature/foodlog` branch
- [ ] Import project in AndroidStudio
- [ ] Verify these files exist:
  - [ ] `foodLog/models/FoodModels.kt`
  - [ ] `foodLog/FoodRepository.kt`
  - [ ] `foodLog/FoodService.kt`
  - [ ] `foodLog/FoodViewModel.kt`
  - [ ] `foodLog/FoodLogScreen.kt`
- [ ] Run project and verify it launches
- [ ] Check that component imports work
- [ ] Understand the FoodService pattern

## Person 3: Dashboard Setup

- [ ] Check out `feature/dashboard` branch
- [ ] Import project in AndroidStudio
- [ ] Verify these files exist:
  - [ ] `dashboard/models/DashboardModels.kt`
  - [ ] `dashboard/DashboardRepository.kt`
  - [ ] `dashboard/NutritionService.kt`
  - [ ] `dashboard/DashboardViewModel.kt`
  - [ ] `dashboard/DashboardScreen.kt`
- [ ] Run project and verify it launches
- [ ] Test BMR calculation logic
- [ ] Review NutritionService formulas

## Person 4: Admin & Integration Setup

- [ ] Check out `feature/admin` branch
- [ ] Import project in AndroidStudio
- [ ] Verify these files exist:
  - [ ] `admin/models/AdminModels.kt`
  - [ ] `admin/AdminRepository.kt`
  - [ ] `admin/AdminService.kt`
  - [ ] `admin/AdminViewModel.kt`
  - [ ] `admin/AdminScreen.kt`
  - [ ] `navigation/Navigation.kt` (updated)
  - [ ] `MainActivity.kt` (updated)
- [ ] Run project and verify it launches
- [ ] Test that drawer navigation is responsive
- [ ] Verify all screens are accessible

## All Team Members

### Compile Check
```bash
./gradlew clean
./gradlew build -x test
```

Expected output:
```
BUILD SUCCESSFUL in Xs
```

If errors appear:
1. Check each error message
2. Most common: Import errors → use IDE autocomplete
3. Hilt errors → check @AndroidEntryPoint, @HiltViewModel
4. Reference errors → verify file exists in correct package

### Code Review Checklist
- [ ] All MVVM patterns followed (ViewModel → Repository → ApiService)
- [ ] All StateFlow patterns consistent
- [ ] All error handling uses Result<T>
- [ ] All Composables have proper state management
- [ ] All colors use Color(0xFF...) from palette

### Dependency Verification
- [ ] ApiService accessible from all repositories
- [ ] ApiClient working with TokenInterceptor
- [ ] Hilt DI providing ApiService
- [ ] Navigation can reach all screens
- [ ] CommonComponents imported where needed

## Backend Team Coordination

### Required API Endpoints
Verify backend implements all endpoints in `core/api/ApiService.kt`:
- [ ] POST /auth/register
- [ ] POST /auth/login
- [ ] GET /auth/profile
- [ ] PUT /auth/profile
- [ ] GET /food/search?query=...
- [ ] GET /food/all
- [ ] GET /food/{id}
- [ ] POST /log/add
- [ ] GET /log/today
- [ ] GET /log/{date}
- [ ] DELETE /log/{logId}
- [ ] GET /dashboard/summary
- [ ] GET /dashboard/nutrition-data?days=7
- [ ] POST /food/propose
- [ ] GET /food/proposals
- [ ] GET /admin/pending-foods
- [ ] POST /admin/approve-proposal/{proposalId}
- [ ] POST /admin/reject-proposal/{proposalId}

### API Response Format
Example: GET /auth/login → POST returns
```json
{
  "token": "jwt_token_here",
  "user": {
    "userId": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "age": 25,
    "weight": 70.5,
    "height": 175.0,
    "gender": "Male",
    "role": "USER"
  }
}
```

## Handoff Checklist (End of Day 1)

### Project Status
- [ ] Code compiles without errors
- [ ] Code compiles without warnings
- [ ] App launches on emulator/device
- [ ] No crashes on startup
- [ ] Navigation drawer opens/closes
- [ ] All screens load without errors

### Git Setup
- [ ] Main branch up to date
- [ ] Feature branches created for each person
- [ ] Each person can commit to their branch
- [ ] PR template ready (if using GitHub)

### Documentation
- [ ] ARCHITECTURE.md reviewed by all
- [ ] TEAM_BRIEFING.md distributed
- [ ] REFACTORING_COMPLETE.md reviewed
- [ ] DAY1_CHECKLIST.md completed

### Team Communication
- [ ] Daily standup scheduled
- [ ] Chat channel created (#nutrilog-dev)
- [ ] Backend team informed of API requirements
- [ ] Code review process defined

---

## 🎯 Success Criteria for Day 1

**Minimum**: Project compiles and launches ✅
**Target**: Each person understands their feature and structure 🎯
**Bonus**: Started implementing their feature 🚀

---

## 🔧 Troubleshooting

### Build fails with "Package not found"
```bash
# Clean Gradle cache
rm -rf ~/.gradle
./gradlew clean
./gradlew build
```

### AndroidStudio can't find imports
- File → Invalidate Caches → Restart

### Emulator crashes
- Try with device via USB debugging
- Or use a different emulator image

### Git conflicts already (shouldn't happen Day 1)
- Ask team lead to review
- Likely caused by accidental commit to wrong branch

---

**Timeline**: Complete by EOD Day 1 ⏰
**Status Update**: Share in team chat when done ✅

