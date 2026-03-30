# QuantityMeasurementApp
## UC-18 : Quantity Measurement App – Google Authentication 
---

This repository contains the Spring Boot backend for the Quantity Measurement Application, enhanced with Google Authentication and User Management using Spring Security, JWT, and OAuth2.

## ⚙️ Core Functionalities  

- 🔐 Secure login with Google  
- 🎫 JWT token generation & validation  
- 👤 User registration & retrieval  
- 🚫 Unauthorized access handling (401 / 403)  

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

| Endpoint            | Description                  |
|--------------------|-----------------------------|
| `/auth/google`     | Google login authentication |
| `/auth/user`       | Get logged-in user details  |
| `/api/**`          | Protected APIs              |

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
- 🧩 Clean architecture  
- 🔄 Easy integration with frontend  
- 👨‍💻 Reduced backend complexity  

## 📌 Conclusion  

This feature implements a **modern authentication system** using:

- **OAuth2 for secure login**
- **JWT for stateless session handling**
- **Spring Security for API protection**

Together, they provide a **robust, scalable, and production-ready backend security system**.

---

## 🔗 Repository Link  

👉 [View Code on GitHub](https://github.com/Saud0786/QuantityMeasurementApp/tree/feature/UC18-GoogleAuthUserManagement)
