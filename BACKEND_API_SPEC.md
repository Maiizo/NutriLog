# Backend API Specification for NutriLog

Dokumen ini menjelaskan struktur database PostgreSQL dan API endpoints yang diperlukan untuk mendukung Frontend Android.

## Database Schema (PostgreSQL)

### 1. Users Table
```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'user', -- 'user' or 'admin'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. Health Profile Table
```sql
CREATE TABLE health_profiles (
    profile_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE,
    age INTEGER NOT NULL,
    gender VARCHAR(20) NOT NULL, -- 'Male', 'Female', 'Other'
    weight_kg FLOAT NOT NULL,
    height_cm FLOAT NOT NULL,
    daily_target_calories FLOAT,
    bmr_result FLOAT, -- Calculated BMR
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_health_profiles_user_id ON health_profiles(user_id);
```

### 3. Food Master Table
```sql
CREATE TABLE foods (
    food_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    calories_per_serving FLOAT NOT NULL,
    protein_g FLOAT NOT NULL,
    fat_g FLOAT NOT NULL,
    carbs_g FLOAT NOT NULL,
    is_approved BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_foods_name ON foods(name);
```

### 4. Daily Log Table
```sql
CREATE TABLE daily_logs (
    log_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    date DATE NOT NULL,
    total_calories_consumed FLOAT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE(user_id, date)
);

CREATE INDEX idx_daily_logs_user_id ON daily_logs(user_id);
CREATE INDEX idx_daily_logs_date ON daily_logs(date);
```

### 5. Log Items Table
```sql
CREATE TABLE log_items (
    item_id SERIAL PRIMARY KEY,
    log_id INTEGER NOT NULL,
    food_id INTEGER NOT NULL,
    serving_quantity FLOAT NOT NULL,
    consumed_calories FLOAT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (log_id) REFERENCES daily_logs(log_id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES foods(food_id) ON DELETE RESTRICT
);

CREATE INDEX idx_log_items_log_id ON log_items(log_id);
```

### 6. Food Proposals Table
```sql
CREATE TABLE food_proposals (
    proposal_id SERIAL PRIMARY KEY,
    proposed_food_name VARCHAR(255) NOT NULL,
    proposed_calories FLOAT NOT NULL,
    proposed_protein_g FLOAT,
    proposed_fat_g FLOAT,
    proposed_carbs_g FLOAT,
    status VARCHAR(50) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
    submitted_by INTEGER NOT NULL,
    approved_by INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (submitted_by) REFERENCES users(user_id),
    FOREIGN KEY (approved_by) REFERENCES users(user_id)
);

CREATE INDEX idx_food_proposals_status ON food_proposals(status);
```

## REST API Endpoints

### Authentication Endpoints

#### POST /api/auth/register
Register user baru dengan health profile data.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe",
  "age": 25,
  "weight": 70.5,
  "height": 175.0,
  "gender": "Male"
}
```

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "userId": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "age": 25,
    "weight": 70.5,
    "height": 175.0,
    "gender": "Male",
    "role": "user"
  }
}
```

#### POST /api/auth/login
Login dengan email & password.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "user": { ... }
}
```

#### GET /api/auth/profile
Get profil user saat ini (requires authentication header).

**Response (200 OK):**
```json
{
  "userId": 1,
  "email": "user@example.com",
  "name": "John Doe",
  "age": 25,
  "weight": 70.5,
  "height": 175.0,
  "gender": "Male",
  "role": "user"
}
```

### Food Endpoints

#### GET /api/food/all
Get semua makanan yang approved.

**Response (200 OK):**
```json
[
  {
    "foodId": 1,
    "name": "Nasi Putih",
    "caloriesPerServing": 150.0,
    "proteinG": 2.7,
    "fatG": 0.3,
    "carbsG": 28.0
  },
  ...
]
```

#### GET /api/food/search?query=
Search makanan berdasarkan nama.

**Response (200 OK):**
```json
[
  {
    "foodId": 1,
    "name": "Nasi Putih",
    "caloriesPerServing": 150.0,
    ...
  }
]
```

### Daily Log Endpoints

#### POST /api/log/add
Tambah makanan ke daily log.

**Request Body:**
```json
{
  "foodId": 1,
  "servingQuantity": 1.5,
  "date": "2024-01-15"
}
```

**Response (201 Created):**
```json
{
  "logId": 1,
  "userId": 1,
  "date": "2024-01-15",
  "items": [
    {
      "itemId": 1,
      "foodId": 1,
      "servingQuantity": 1.5,
      "consumedCalories": 225.0,
      "food": { ... }
    }
  ],
  "totalCaloriesConsumed": 225.0
}
```

#### GET /api/log/today
Get log hari ini.

**Response (200 OK):**
```json
{
  "logId": 1,
  "userId": 1,
  "date": "2024-01-15",
  "items": [...],
  "totalCaloriesConsumed": 225.0
}
```

#### GET /api/log/{date}
Get log untuk tanggal spesifik.

**Response (200 OK):**
```json
{
  "logId": 1,
  "userId": 1,
  "date": "2024-01-15",
  "items": [...],
  "totalCaloriesConsumed": 225.0
}
```

### Dashboard Endpoints

#### GET /api/dashboard/summary
Get dashboard summary dengan BMR dan nutrition data.

**Response (200 OK):**
```json
{
  "todayCalories": 1250.5,
  "targetCalories": 2000.0,
  "bmr": 1600.0,
  "healthProfile": {
    "profileId": 1,
    "age": 25,
    "gender": "Male",
    "weightKg": 70.5,
    "heightCm": 175.0,
    "dailyTargetCalories": 2000.0,
    "bmrResult": 1600.0
  },
  "recentLogs": [...]
}
```

#### GET /api/dashboard/nutrition-data?days=7
Get nutrition data untuk N hari terakhir.

**Response (200 OK):**
```json
[
  {
    "logId": 1,
    "userId": 1,
    "date": "2024-01-15",
    "items": [...],
    "totalCaloriesConsumed": 225.0
  },
  ...
]
```

### Food Proposal Endpoints

#### POST /api/food/propose
User propose makanan baru.

**Request Body:**
```json
{
  "proposedFoodName": "Gado-gado Special",
  "proposedCalories": 350.0
}
```

**Response (201 Created):**
```json
{
  "proposalId": 1,
  "proposedFoodName": "Gado-gado Special",
  "proposedCalories": 350.0,
  "status": "PENDING",
  "submittedBy": 1,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

## Authentication

Semua endpoint (kecuali login & register) memerlukan JWT token di header:

```
Authorization: Bearer <token>
```

## BMR Calculation

Menggunakan Mifflin-St Jeor formula:

**Untuk Pria:**
```
BMR = (10 × weight_kg) + (6.25 × height_cm) - (5 × age) + 5
```

**Untuk Wanita:**
```
BMR = (10 × weight_kg) + (6.25 × height_cm) - (5 × age) - 161
```

Daily target calories biasanya: BMR × 1.55 (moderate activity)

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid input data",
  "details": "Email already exists"
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "details": "Invalid or expired token"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found",
  "details": "Food with ID 999 not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "details": "An unexpected error occurred"
}
```

## Notes

- Semua password harus di-hash dengan bcrypt atau similar
- Token JWT harus expire setelah 24 jam
- Implementasi rate limiting untuk security
- Validate semua input di server side
- Return timestamp dalam ISO 8601 format
