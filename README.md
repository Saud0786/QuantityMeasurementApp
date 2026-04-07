# 📏 Quantity Measurement System - Microservices Architecture

## 🚀 UC-21: Microservices Implementation

---

## 📌 Overview

A scalable **Quantity Measurement System** built using **Microservices Architecture**.
It allows users to **convert, compare, and perform operations** on quantities like:

* 📏 Length
* ⚖️ Weight
* 💧 Volume
* 🌡️ Temperature

Integrated with **JWT Authentication + Google OAuth2 Login + API Gateway + Service Discovery**

---

## 🏗️ Architecture

```
microservice-architecture/
│
├── eureka-server/                 # Service Discovery
├── api-gateway/                   # API Gateway (JWT Filter)
├── auth-service/                  # Authentication Service
├── quantity-service/              # Core Business Logic
├── history-service/               # Operation History
├── admin-server/                  # Monitoring (Spring Boot Admin)
├── frontend/                      # React App
└── docker-compose.yml             # Docker Setup
```

---

## ⚙️ Tech Stack

* ☕ Java 21
* 🌱 Spring Boot 3
* ☁️ Spring Cloud
* 🔐 Spring Security + JWT
* 🔑 OAuth2 (Google Login)
* 🗄️ MySQL
* ⚛️ React (Frontend)
* 🐳 Docker

---

## 🔐 Authentication Features

* ✅ JWT Authentication (Secure Cookies)
* ✅ Google OAuth2 Login
* ✅ Role-based Access (USER / ADMIN)
* ✅ Token Validation at API Gateway

---

## 📐 Features

### 🔹 Supported Operations

| Operation | Length | Weight | Volume | Temperature |
| --------- | :----: | :----: | :----: | :---------: |
| Convert   |    ✅   |    ✅   |    ✅   |      ✅      |
| Compare   |    ✅   |    ✅   |    ✅   |      ✅      |
| Add       |    ✅   |    ✅   |    ✅   |      ❌      |
| Subtract  |    ✅   |    ✅   |    ✅   |      ❌      |
| Multiply  |    ✅   |    ✅   |    ✅   |      ❌      |
| Divide    |    ✅   |    ✅   |    ✅   |      ❌      |

---

## 📊 Microservices

### 🔹 Eureka Server

* Service registry
* All services register here

### 🔹 API Gateway

* Single entry point
* JWT validation
* Routing to services

### 🔹 Auth Service

* Login / Register
* JWT token generation
* Google OAuth integration

### 🔹 Quantity Service

* Core logic
* Unit conversion & operations

### 🔹 History Service

* Stores all operations
* Filters & statistics

### 🔹 Admin Server

* Monitor all microservices
* Health checks

---

## 📁 Folder Structure (Detailed)

```
microservice-architecture/
│
├── eureka-server/
│   └── src/main/java/...
│
├── api-gateway/
│   └── src/main/java/com/app/gateway/
│
├── auth-service/
│   └── src/main/java/com/app/auth/
│
├── quantity-service/
│   └── src/main/java/com/app/quantity/
│
├── history-service/
│   └── src/main/java/com/app/history/
│
├── admin-server/
│
├── frontend/
│   ├── components/
│   ├── pages/
│   └── services/
│
└── docker-compose.yml
```

---

## 🚀 Run Project

### 🔹 Backend Services

```bash
# Eureka Server
cd eureka-server
mvn spring-boot:run

# Auth Service
cd auth-service
mvn spring-boot:run

# Quantity Service
cd quantity-service
mvn spring-boot:run

# History Service
cd history-service
mvn spring-boot:run

# API Gateway
cd api-gateway
mvn spring-boot:run
```

---

### 🔹 Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 🔗 API Flow

```
Frontend → API Gateway → Auth / Quantity / History Services
```

---

## 📌 Important Notes

* ❌ Do NOT push:

  * application.yml
  * .env files
* ✅ Use environment variables for secrets

---

## 🧠 Learnings

* Microservices Architecture
* API Gateway pattern
* Service Discovery
* JWT Authentication
* OAuth2 Integration
* Inter-service communication (Feign)

---

---

## 🌐 System Design Highlights

* 🔹 **Microservices Architecture** with loosely coupled services
* 🔹 **API Gateway Pattern** for centralized routing and security
* 🔹 **Service Discovery (Eureka)** for dynamic service registration
* 🔹 **Inter-service Communication** using OpenFeign
* 🔹 **Centralized Monitoring** via Spring Boot Admin
* 🔹 **Scalable & Maintainable Design**

---

## 🔄 Request Flow (Detailed)

```text
Client (React App)
        ↓
API Gateway (JWT Filter + Routing)
        ↓
---------------------------------------
| Auth Service | Quantity Service | History Service |
---------------------------------------
        ↓
     MySQL Database
```

---

## 🔐 Security Implementation

* 🔒 Password encryption using BCrypt
* 🔒 JWT token generation & validation
* 🔒 Secure HttpOnly cookies
* 🔒 OAuth2 login with Google
* 🔒 CORS configuration for frontend-backend communication
* 🔒 Endpoint-level authorization

---

## 📡 API Endpoints (Sample)

### 🔹 Auth Service

```http
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/user
```

### 🔹 Quantity Service

```http
POST /api/quantity/convert
POST /api/quantity/compare
POST /api/quantity/add
POST /api/quantity/subtract
```

### 🔹 History Service

```http
GET /api/history
GET /api/history/filter
```

---

## 🧪 Testing

* ✅ Unit Testing using JUnit
* ✅ API Testing using Postman
* ✅ Integration Testing for service communication

---

## 🐳 Docker Support

Run entire system using Docker:

```bash
docker-compose up --build
```

---

## 📊 Monitoring & Health Check

* Spring Boot Actuator endpoints:

```bash
/actuator/health
/actuator/metrics
```

* Spring Boot Admin Dashboard:

```bash
http://localhost:9090
```

---

## ⚡ Performance Considerations

* 🚀 Load balancing using Spring Cloud
* 🚀 Stateless authentication using JWT
* 🚀 Efficient DB operations with JPA
* 🚀 Reduced coupling between services

---

## 🔮 Future Enhancements

* 🔹 Redis caching for faster responses
* 🔹 Kafka for event-driven communication
* 🔹 CI/CD pipeline (GitHub Actions)
* 🔹 Kubernetes deployment
* 🔹 Rate limiting at API Gateway

---

## 📸 Screenshots UI

<img width="1917" height="811" alt="image" src="https://github.com/user-attachments/assets/366f82d3-475a-4936-b1db-7e555e5c1e3a" />


---

## 🏆 Why This Project Stands Out

* ✅ End-to-end full-stack project
* ✅ Real-world microservices architecture
* ✅ Security + OAuth2 integration
* ✅ Scalable and production-ready design
* ✅ Covers backend + frontend + DevOps

---

## ⭐ Support
 ⭐ [GitHub Link](https://github.com/Saud0786/QuantityMeasurementApp/tree/feature/UC21-MicroservicesArchitecture)
 
