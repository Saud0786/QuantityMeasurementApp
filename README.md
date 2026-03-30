# 🚀 Quantity Measurement App 

- Conversion between different units (e.g., cm ↔ m, kg ↔ g)  
- Comparison of quantities across units  
- Arithmetic operations like addition and subtraction of quantities  
- Handling of multiple measurement domains such as:
  - Length  
  - Weight  
  - Volume  
  - Temperature  

This ensures **accuracy, consistency, and flexibility** in handling real-world measurement problems.
---

This repository contains the Spring Boot backend for the Quantity Measurement Application.
The application is designed to perform **accurate unit measurement conversions and arithmetic operations on quantities** such as length, weight, volume, and temperature.  
It also integrates a **secure authentication system using Spring Security, JWT, and Google OAuth2** to ensure safe and scalable user access.

---

## 📏 About Quantity Measurement  

The Quantity Measurement system enables:

## ⚙️ Core Functionalities  

- 📏 Unit measurement & conversion  
- ➕ Arithmetic operations on quantities  
- 🔐 Secure authentication with JWT & OAuth2  
- 👤 User management system  
- 🚫 Unauthorized access handling (401 / 403)  

---

## 🌳 Git Workflow

```
main
└── dev
    ├── feature/UC1-FeetEquality
    ├── feature/UC2-InchEquality
    ├── feature/UC3-GenericLength
    ├── feature/UC4-YardEquality
    ├── feature/UC5-UnitConversoion
    ├── feature/UC6-UnitAddition
    ├── feature/UC7-TargetUnitAddition
    ├── feature/UC8-StandaloneUnit
    ├── feature/UC9-WeightMeasurement
    ├── feature/UC10-GenericQuantity
    ├── feature/UC11-VolumeMeasurement
    ├── feature/UC12-SubtractionAndDivision
    ├── feature/UC13-CentralizedArithmeticLogic
    ├── feature/UC14-TemperatureMeasurementwithSelectiveArithmetic
    ├── feature/UC15-NTierArchitectureRefactor
    ├── feature/UC16-JDBCPersistence
    ├── feature/UC17-SpringBackend
    └── feature/UC18-GoogleAuthUserManagement
```
---

## 📅 UC1 – Feet Measurement Equality (17 Feb 2026)

**Branch:** `feature/UC1-FeetEquality`

### 🎯 Objective
- Validate equality of two Feet measurements  
- Implement proper `equals()` method  
- Follow TDD approach  

### ✅ Implementation
- Created Feet class  
- Implemented equality logic  
- Handled null and type safety  
- Wrote JUnit 5 test cases  

---

## 📅 UC2 – Feet and Inches Measurement Equality (18 Feb 2026)

**Branch:** `feature/UC2-InchEquality`

### 🎯 Objective
- Compare Feet and Inches  
- Ensure 12 inches = 1 foot  

### ✅ Implementation
- Introduced conversion logic  
- Implemented base unit comparison  
- Improved equality handling  

---

## 📅 UC3 – Generic Quantity Class (19 Feb 2026)

**Branch:** `feature/UC3-GenericLength`

### 🎯 Objective
- Remove duplication  
- Apply DRY principle  

### ✅ Implementation
- Centralized conversion logic  
- Removed unit-specific duplication  
- Improved abstraction  

---

## 📅 UC4 – Extended Unit Support (20 Feb 2026)

**Branch:** `feature/UC4-YardEquality`

### 🎯 Objective
- Support additional units  
- Make system scalable  

### ✅ Implementation
- Introduced Unit Enum  
- Base unit conversion mapping  

---

## 📅 UC5 – Unit-to-Unit Conversion (20 Feb 2026)

**Branch:** `feature/UC5-UnitConversoion`

### 🎯 Objective
- Convert one unit into another  

### ✅ Implementation
- Implemented `convertTo()` method  
- Precision-safe calculations  

---

## 📅 UC6 – Addition of Two Length Units (20 Feb 2026)

**Branch:** `feature/UC6-UnitAddition`

### 🎯 Objective
- Add two quantities correctly  

### ✅ Implementation
- Converted to base unit before addition  
- Accurate arithmetic operations  

---

## 📅 UC7 – Target Unit Addition (20 Feb 2026)

**Branch:** `feature/UC7-TargetUnitAddition`

### 🎯 Objective
- Return result in specified unit  

### ✅ Implementation
- Implemented `add(quantity, targetUnit)`  

---

## 📅 UC8 – Standalone Unit Refactor (21 Feb 2026)

**Branch:** `feature/UC8-StandaloneUnit`

### 🎯 Objective
- Improve modularity  

### ✅ Implementation
- Separated Unit enum  
- Improved flexibility  

---

## 📅 UC9 – Weight Measurement (21 Feb 2026)

**Branch:** `feature/UC9-WeightMeasurement`

### 🎯 Objective
- Add weight category  

### ✅ Implementation
- Added Gram, Kilogram  
- Prevented cross-category comparison  

---

## 📅 UC10 – Generic Multi-Category System (21 Feb 2026)

**Branch:** `feature/UC10-GenericQuantity`

### 🎯 Objective
- Fully generic system  

### ✅ Implementation
- Introduced Unit interface  
- Type-safe operations  

---

## 📅 UC11 – Volume Measurement (22 Feb 2026)

**Branch:** `feature/UC11-VolumeMeasurement`

### 🎯 Objective
- Add volume category  

### ✅ Implementation
- Added Litre, Millilitre  
- Enabled conversions  

---

## 📅 UC12 – Subtraction & Division (23 Feb 2026)

**Branch:** `feature/UC12-SubtractionAndDivision`

### 🎯 Objective
- Extend arithmetic operations  

### ✅ Implementation
- Added `subtract()` and `divide()`  

---

## 📅 UC13 – Centralized Arithmetic Logic (23 Feb 2026)

**Branch:** `feature/UC13-CentralizedArithmeticLogic`

### 🎯 Objective
- Remove duplication  

### ✅ Implementation
- Centralized arithmetic handler  

---

## 📅 UC14 – Temperature Measurement (24 Feb 2026)

**Branch:** `feature/UC14-TemperatureMeasurementwithSelectiveArithmetic`

### 🎯 Objective
- Support temperature  

### ⚠ Special Note
Temperature uses formula-based conversion:  

°F = (°C × 9/5) + 32

### ✅ Implementation
- Created TemperatureUnit enum  
- Implemented formula-based conversion  
- Allowed equality comparison  
- Restricted invalid arithmetic  

---

## 📅 UC15 – N-Tier Architecture (09 Mar 2026)

**Branch:** `feature/UC15-NTierArchitectureRefactor`

### 🏗 Architecture

Controller → Service → Repository → Database

### ✅ Highlights
- DTO Layer  
- Dependency Injection  
- SOLID Principles  

---

## 📅 UC16 – JDBC Integration (11 Mar 2026)

**Branch:** `feature/UC16-JDBCPersistence`

### ✅ Implementation
- JDBC + SQL  
- Connection Pooling  
- Prepared Statements  
- Repository Layer  

---

## 📅 UC17 – Spring Boot Backend (12 Mar 2026)

**Branch:** `feature/UC17-SpringBackend`

### ✅ Implementation
- REST APIs  
- Spring Data JPA  
- Service Layer  
- Logging  

---

## 📅 UC18 – Google Auth & JWT (20 Mar 2026)

**Branch:** `feature/UC18-GoogleAuthUserManagement`

### 🔐 Features
- Google OAuth2 Login  
- JWT Authentication  
- Secure APIs  
- User Role Management  

---

## 🏁 Final Outcome

✔ Supports:
- Length  
- Weight  
- Volume  
- Temperature  

✔ Built with:
- Clean Architecture  
- Scalable Design  
- Industry Practices  


## ✨ Features Implemented

### 🔐 Authentication & Security
- Spring Security integration  
- JWT (JSON Web Token) based authentication  
- Secure REST APIs  
- Role-based access (if implemented)  

### 🌐 OAuth2 Login
- Google Authentication using OAuth2  
- Seamless login/signup with Google account  
- Automatic user registration on first login  

### 👤 User Management
- User entity and database integration  
- Store authenticated user details  
- Manage user sessions securely  

### ⚙️ Backend Functionalities
- RESTful API design  
- Exception handling  
- Secure endpoints  
- Token validation and filtering  

---

## 🛠️ Tech Stack

| Technology       | Description                              |
|------------------|-------------------------------------------|
| Java             | Programming Language                      |
| Spring Boot      | Backend Framework                         |
| Spring Security  | Authentication & Authorization            |
| JWT              | Token-based authentication                |
| OAuth2           | Google Login Integration                  |
| Hibernate / JPA  | ORM for database interaction              |
| MySQL / H2       | Database (configurable)                   |
| Maven            | Build Tool                                |

---

## 📁 Project Structure

    src/
     ├── controller        # REST Controllers
     ├── service           # Business Logic
     ├── repository        # Database Layer
     ├── model             # Entity Classes
     ├── security          # JWT & OAuth2 Configurations
     ├── config            # App Configurations
     └── exception         # Global Exception Handling

---

## 📂 API Endpoints (Sample)  

| Endpoint                      | Description                          |
|------------------------------|--------------------------------------|
| `/auth/login`                | JWT-based login                     |
| `/oauth2/authorize/google`   | Google OAuth2 login                 |
| `/auth/user`                 | Get authenticated user details      |
| `/api/**`                    | Protected APIs                      |

---

## 🔄 Authentication Flow

### 🔑 JWT Authentication Flow
- User sends login request  
- Backend validates credentials  
- JWT token is generated  
- Token is sent to client  
- Client includes token in headers for API requests  
- Backend validates token for each request  

### 🌍 Google OAuth2 Flow
- User clicks "Login with Google"  
- Redirect to Google Authentication page  
- Google verifies user credentials  
- Backend receives user details  
- User is registered (if new)  
- JWT token is generated and returned  

---

## 📊 Advantages  

- 🔐 High security (OAuth2 + JWT)  
- ⚡ Scalable (stateless backend)  
- 🧩 Clean and maintainable architecture  
- 🔄 Easy integration with frontend applications  
- 👨‍💻 Reduced backend complexity  

---

## 📌 Conclusion  

This project implements a **modern backend system** with:

- **Robust quantity measurement and conversion logic**
- **Secure authentication using OAuth2 and JWT**
- **Scalable and maintainable architecture using Spring Boot**

It is designed to be **production-ready, secure, and easily extendable**.

---

## 🔗 Repository Link  
👉 [View Code on GitHub](https://github.com/Saud0786/QuantityMeasurementApp/tree/feature/UC18-GoogleAuthUserManagement)
