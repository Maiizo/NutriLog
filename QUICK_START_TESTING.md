# 🚀 Quick Start Guide - NutriLog Frontend

Step-by-step guide untuk quickly setup dan test NutriLog Android app.

## ⚡ 5-Minute Setup

### Step 1: Clone & Open Project
```bash
# Jika belum di-clone
git clone <your-repo>

# Buka di Android Studio
File → Open → Select NutriLog folder
```

### Step 2: Sync Gradle
```
File → Sync Now (or Ctrl+Shift+I)
```

Wait untuk gradle sync selesai (~3-5 menit untuk first time)

### Step 3: Update API Base URL

Edit file: `app/src/main/java/com/chelsea/nutrilog/data/api/ApiClient.kt`

Cari baris:
```kotlin
private const val BASE_URL = "http://192.168.1.1:8000/api/"
```

Ganti `192.168.1.1` dengan:
- **Local testing**: `10.0.2.2:8000` (Android emulator default)
- **Real server**: Your actual server IP/domain

### Step 4: Build & Run

```
Run → Run 'app' (atau Shift+F10)
```

Pilih emulator atau physical device

## 🧪 Test Scenarios

### Scenario 1: Complete Login & Dashboard Flow

**Test Path:**
1. App opens → LoginScreen muncul
2. Click "Register" link
3. Fill form:
   - Email: `test@example.com`
   - Password: `password123`
   - Name: `Test User`
   - Age: `25`
   - Weight: `70`
   - Height: `175`
   - Gender: `Male`
4. Click Register button
5. Wait untuk API response
6. Dashboard screen should load
7. Check BMR & calories display
8. Click menu icon (hamburger)
9. Click "Logout"
10. Back ke login screen

**Expected Results:**
- ✅ Register successful dengan JWT token
- ✅ Auto redirect ke dashboard
- ✅ Dashboard shows user data
- ✅ Logout clears session

---

### Scenario 2: Food Logging Flow

**Test Path:**
1. Start dari Dashboard
2. Click "+" button (FAB)
3. FoodLogScreen opens
4. Type "rice" di search bar
5. Wait untuk search results
6. Click pada makanan (nasi)
7. Input serving quantity: `1.5`
8. Click "Add"
9. Dialog closes
10. Navigate back ke dashboard
11. Refresh dashboard

**Expected Results:**
- ✅ Search returns food items
- ✅ Dialog shows quantity input
- ✅ Food logged ke daily log
- ✅ Dashboard calories updated

---

### Scenario 3: Error Handling

**Test Path 1 - Invalid Login:**
1. Input wrong email format
2. Click Login
3. Observe error message

**Test Path 2 - Network Error:**
1. Disconnect internet / stop backend
2. Try login
3. Error message displayed

**Expected Results:**
- ✅ Form validation errors shown
- ✅ Network errors caught
- ✅ Loading state canceled
- ✅ UI remains responsive

---

### Scenario 4: Navigation

**Test Path:**
1. Login → Dashboard
2. Click menu → Click "Log Food"
3. Search something
4. Back button → Back to dashboard
5. Click menu → Click "Dashboard"
6. Click menu → Click "Logout"

**Expected Results:**
- ✅ All navigations smooth
- ✅ State preserved correctly
- ✅ No crashes
- ✅ Drawer closes properly

## 🐛 Debugging Tips

### Check Logs
```bash
# Terminal di Android Studio
adb logcat | grep "NutriLog"
```

### Network Debugging
- Enable network logging in ApiClient.kt (already enabled)
- Check logcat untuk request/response
- Use Charles Proxy atau Fiddler untuk intercept

### Common Issues

**Issue**: "failed to connect to /10.0.2.2:8000"
- **Solution**: Backend belum running, atau IP salah di ApiClient.kt

**Issue**: "Null pointer exception di StateFlow"
- **Solution**: ViewModel belum initialize, check Hilt setup

**Issue**: "API response 401 Unauthorized"
- **Solution**: Token expired atau invalid, clear SharedPreferences

### SharedPreferences Clear (for testing)
```kotlin
// Add ini ke MainActivity untuk debug
context.getSharedPreferences("auth", Context.MODE_PRIVATE).edit().clear().apply()
```

## 📱 Emulator Setup (if needed)

### Create New Emulator
```
Tools → Device Manager → Create Device
- Device: Pixel 6 Pro
- API Level: 33+ (minimum 28)
- RAM: 2GB+
- Storage: 2GB+
```

### Launch Emulator
```
Device Manager → Select emulator → Play button
```

## 🔍 UI Elements to Check

### LoginScreen
- [ ] Email textfield dengan validation
- [ ] Password textfield dengan eye icon (optional)
- [ ] Login button dengan loading state
- [ ] Register link
- [ ] Error message display

### RegisterScreen
- [ ] All input fields present (email, password, name, age, weight, height, gender)
- [ ] Back button works
- [ ] Form validation
- [ ] Gender filter chips

### DashboardScreen
- [ ] BMR card displayed
- [ ] Today's calories card
- [ ] Health profile card
- [ ] Filter chips (7/14/30 days)
- [ ] Recent logs list
- [ ] FAB button untuk log food
- [ ] Menu button untuk drawer

### FoodLogScreen
- [ ] Search bar
- [ ] Food list items
- [ ] Food item clickable
- [ ] Serving dialog
- [ ] Add button works
- [ ] Back button works

### Navigation Drawer
- [ ] Drawer opens/closes
- [ ] Menu items clickable
- [ ] Logout button clear session
- [ ] Shows current user email

## 📊 Expected UI Behavior

| Action | Expected Behavior | Status |
|--------|-------------------|--------|
| Open app | LoginScreen loads | ✅ |
| Click Register | Navigate to RegisterScreen | ✅ |
| Fill & submit register | JWT token saved, redirect dashboard | ✅ |
| Login with credentials | Session created, redirect dashboard | ✅ |
| Click + button | FoodLogScreen opens | ✅ |
| Search food | Results displayed | ✅ |
| Click food item | Serving dialog shows | ✅ |
| Submit food log | Daily log updated | ✅ |
| View dashboard | Shows calories & BMR | ✅ |
| Click menu button | Drawer slides in | ✅ |
| Click logout | Session cleared, back to login | ✅ |

## ✨ Visual Design Checks

- [x] Green color scheme (#4CAF50) applied
- [x] Consistent spacing (16dp padding standard)
- [x] Material Design 3 components used
- [x] Loading indicators present
- [x] Error states visible
- [x] Buttons have proper elevation
- [x] Text readable on all backgrounds
- [x] Forms have proper labels

## 🎯 Performance Checks

**Measure:**
1. App startup time: Should be < 3 seconds
2. Login response: Should be < 2 seconds (network dependent)
3. Dashboard load: Should be < 1 second (cache if available)
4. Search response: Should be < 1 second

**Use:** Android Studio Profiler
```
Run → Debug 'app' → Profiler tab
```

## 🚨 Critical Path Testing

**Must Work Before Release:**
1. ✅ Register → Creates account → Login works
2. ✅ Login → Displays dashboard → Shows user data
3. ✅ Dashboard → FAB opens food log → Search works
4. ✅ Food log → Select food → Add to daily log → Calories updated
5. ✅ Logout → Session cleared → Back to login

## 📝 Test Report Template

```markdown
## NutriLog Frontend Test Report
Date: [DATE]
Tester: [NAME]
Device: [DEVICE/EMULATOR]
API Server: [IP:PORT]

### Test Results
- [ ] Register flow: PASS/FAIL
- [ ] Login flow: PASS/FAIL
- [ ] Dashboard display: PASS/FAIL
- [ ] Food logging: PASS/FAIL
- [ ] Navigation: PASS/FAIL
- [ ] Error handling: PASS/FAIL

### Issues Found
1. ...
2. ...

### Notes
...
```

## 🔄 Continuous Testing

After each change:
```bash
# Build & run
gradlew build
gradlew installDebug

# Run basic smoke tests
- Open app
- Login
- Check dashboard
- Logout
```

## 📞 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| App won't build | `Build → Clean Project` then `Build → Rebuild Project` |
| Stuck on login | Check API URL, check backend running |
| Dashboard blank | Check network, check API response |
| Logout not working | Clear cache via Settings app |
| UI elements misaligned | Check screen orientation, try different device |

## ✅ Sign-Off Checklist

Before considering testing complete:
- [ ] All 4 screens load without crashes
- [ ] Navigation works smoothly
- [ ] No unhandled exceptions di logcat
- [ ] Loading states visible
- [ ] Error messages display properly
- [ ] Input validation works
- [ ] Forms submit successfully
- [ ] Logout clears session
- [ ] Can login again after logout
- [ ] UI is responsive
- [ ] No ANR (Application Not Responding)

---

**🎉 If all checklist items pass, frontend is ready for integration testing with backend!**
