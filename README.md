# QuantityMeasurementApp
## UC17 — Spring Boot REST API Migration: Quantity Measurement App

### What is UC17?

UC17 is the final and most comprehensive use case in the Quantity Measurement project. It migrates the standalone Java application built across UC1–UC16 into a fully-featured **Spring Boot REST API**. The goal is to expose all quantity measurement operations — compare, convert, add, subtract, multiply, and divide — as RESTful HTTP endpoints backed by a persistent database, secured by Spring Security, documented with Swagger, and fully tested with both unit and integration tests.

---

## Why Spring Boot?

In the earlier use cases (UC1–UC16), the Quantity Measurement logic lived entirely inside Java classes and was tested only through JUnit. While this validated the business logic, it had no way to be consumed by external clients like web apps or mobile apps.

Spring Boot solves this by wrapping the existing logic inside a production-ready web server. It provides:
- **Embedded Tomcat** — no need to deploy to an external server
- **Auto-configuration** — minimal setup for JPA, Security, Swagger
- **REST support** — expose endpoints via `@RestController`
- **Dependency Injection** — clean separation of concerns via `@Service`, `@Repository`

---

## Architecture

The application follows a standard **Layered Architecture**:

```
Client (Postman / Swagger UI)
        ↓
[ Controller Layer ]   → Receives HTTP requests, validates input
        ↓
[ Service Layer ]      → Contains business logic, unit conversion
        ↓
[ Repository Layer ]   → Persists data to H2 database via JPA
        ↓
[ Database (H2) ]      → In-memory relational database
```

---

## Key Components

### 1. JPA Entity
`QuantityMeasurementEntity` maps to the `quantity_measurement_entity` table. Every operation — whether successful or errored — is persisted to the database. This allows full history tracking.

### 2. Repository
`QuantityMeasurementRepository` extends `JpaRepository` and provides custom query methods like `findByOperation`, `findByThisMeasurementType`, `countByOperationAndIsErrorFalse`, and `findByIsErrorTrue`.

### 3. DTOs
Three DTOs handle data transfer cleanly:
- `QuantityDTO` — holds a single quantity with value, unit, and measurement type. Includes `@NotNull`, `@Pattern`, and `@AssertTrue` validation.
- `QuantityInputDTO` — wraps `thisQuantityDTO`, `thatQuantityDTO`, and optional `targetQuantityDTO` for requests.
- `QuantityMeasurementDTO` — the response object, with static factory methods `fromEntity()` and `toEntity()`.

### 4. Service Layer
`QuantityMeasurementServiceImpl` converts DTOs to `Quantity<IMeasurable>` objects using the unit enums from UC1–UC16, performs the operation, saves the result to the database, and returns a `QuantityMeasurementDTO`.

### 5. Controller
`QuantityMeasurementController` exposes 12 REST endpoints under `/api/v1/quantities`. It delegates all logic to the service layer and handles no business logic itself.

### 6. Global Exception Handler
`GlobalExceptionHandler` uses `@ControllerAdvice` to catch all exceptions in one place and return structured JSON error responses with proper HTTP status codes.

### 7. Security Config
`SecurityConfig` disables CSRF (appropriate for stateless REST APIs), enables CORS, sets session management to `STATELESS`, and permits all requests for development.

### 8. Swagger / OpenAPI
Springdoc OpenAPI 2.3.0 auto-generates interactive API documentation. Every endpoint is annotated with `@Operation`, `@ApiResponse`, and `@ExampleObject` so developers can understand and test the API directly from the browser.

---

## REST Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/compare` | Compare two quantities |
| POST | `/convert` | Convert to another unit |
| POST | `/add` | Add two quantities |
| POST | `/add-with-target-unit` | Add with explicit result unit |
| POST | `/subtract` | Subtract two quantities |
| POST | `/subtract-with-target-unit` | Subtract with explicit result unit |
| POST | `/multiply` | Multiply two quantities |
| POST | `/divide` | Divide two quantities |
| GET | `/history/operation/{op}` | Get history by operation |
| GET | `/history/type/{type}` | Get history by measurement type |
| GET | `/history/errored` | Get all errored records |
| GET | `/count/{operation}` | Count successful operations |

---

## Validation

Input validation is handled by Jakarta Bean Validation on `QuantityDTO`:
- `@NotNull` — value, unit, and measurementType cannot be null
- `@Pattern` — measurementType must match `LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit`
- `@AssertTrue` — unit must be valid for the given measurement type (e.g. `FEET` is valid for `LengthUnit` but not `VolumeUnit`)

If validation fails, `GlobalExceptionHandler` catches `MethodArgumentNotValidException` and returns `400 BAD REQUEST` with a descriptive message.

---

## Database

H2 in-memory database is used for development and testing. Every operation is saved to `quantity_measurement_entity` with full input/output details and an `is_error` flag. This enables history tracking, error auditing, and operation counts.

---

## Testing

### Unit Tests — `QuantityMeasurementControllerTest` (4 tests)
Uses `@WebMvcTest` + `@MockBean` to test the controller layer in isolation. Spring Security filters are disabled with `@AutoConfigureMockMvc(addFilters = false)`.

### Integration Tests — `QuantityMeasurementApplicationTests` (17 tests)
Uses `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate` to start a real server and make actual HTTP requests. Tests are ordered with `@TestMethodOrder` to ensure data is available for history/count tests.

### Unit Tests — `QuantityMeasurementAppTest` (53 tests)
Tests the core business logic of `Quantity.java` and unit enums directly, carried over from UC1–UC16.

### Total: 74 tests — 0 Failures, 0 Errors ✅

---

## Test Report

Maven Surefire Plugin generates an HTML test report:

```
Goals: test surefire-report:report
Report: target/site/surefire-report.html
```

---

## Live URLs

| URL | Purpose |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Swagger UI |
| `http://localhost:8080/api-docs` | OpenAPI JSON |
| `http://localhost:8080/h2-console` | H2 Database Console |
| `http://localhost:8080/actuator/health` | Health Check |

---

## Summary

UC17 brings together everything built in UC1–UC16 and wraps it in a production-ready Spring Boot application. It demonstrates clean layered architecture, RESTful API design, input validation, exception handling, database persistence, security configuration, API documentation, and comprehensive test coverage — all in a single cohesive project.

---


**Link:** [feature/UC17-SpringBackend](# QuantityMeasurementApp
## UC17 — Spring Boot REST API Migration: Quantity Measurement App

### What is UC17?

UC17 is the final and most comprehensive use case in the Quantity Measurement project. It migrates the standalone Java application built across UC1–UC16 into a fully-featured **Spring Boot REST API**. The goal is to expose all quantity measurement operations — compare, convert, add, subtract, multiply, and divide — as RESTful HTTP endpoints backed by a persistent database, secured by Spring Security, documented with Swagger, and fully tested with both unit and integration tests.

---

## Why Spring Boot?

In the earlier use cases (UC1–UC16), the Quantity Measurement logic lived entirely inside Java classes and was tested only through JUnit. While this validated the business logic, it had no way to be consumed by external clients like web apps or mobile apps.

Spring Boot solves this by wrapping the existing logic inside a production-ready web server. It provides:
- **Embedded Tomcat** — no need to deploy to an external server
- **Auto-configuration** — minimal setup for JPA, Security, Swagger
- **REST support** — expose endpoints via `@RestController`
- **Dependency Injection** — clean separation of concerns via `@Service`, `@Repository`

---

## Architecture

The application follows a standard **Layered Architecture**:

```
Client (Postman / Swagger UI)
        ↓
[ Controller Layer ]   → Receives HTTP requests, validates input
        ↓
[ Service Layer ]      → Contains business logic, unit conversion
        ↓
[ Repository Layer ]   → Persists data to H2 database via JPA
        ↓
[ Database (H2) ]      → In-memory relational database
```

---

## Key Components

### 1. JPA Entity
`QuantityMeasurementEntity` maps to the `quantity_measurement_entity` table. Every operation — whether successful or errored — is persisted to the database. This allows full history tracking.

### 2. Repository
`QuantityMeasurementRepository` extends `JpaRepository` and provides custom query methods like `findByOperation`, `findByThisMeasurementType`, `countByOperationAndIsErrorFalse`, and `findByIsErrorTrue`.

### 3. DTOs
Three DTOs handle data transfer cleanly:
- `QuantityDTO` — holds a single quantity with value, unit, and measurement type. Includes `@NotNull`, `@Pattern`, and `@AssertTrue` validation.
- `QuantityInputDTO` — wraps `thisQuantityDTO`, `thatQuantityDTO`, and optional `targetQuantityDTO` for requests.
- `QuantityMeasurementDTO` — the response object, with static factory methods `fromEntity()` and `toEntity()`.

### 4. Service Layer
`QuantityMeasurementServiceImpl` converts DTOs to `Quantity<IMeasurable>` objects using the unit enums from UC1–UC16, performs the operation, saves the result to the database, and returns a `QuantityMeasurementDTO`.

### 5. Controller
`QuantityMeasurementController` exposes 12 REST endpoints under `/api/v1/quantities`. It delegates all logic to the service layer and handles no business logic itself.

### 6. Global Exception Handler
`GlobalExceptionHandler` uses `@ControllerAdvice` to catch all exceptions in one place and return structured JSON error responses with proper HTTP status codes.

### 7. Security Config
`SecurityConfig` disables CSRF (appropriate for stateless REST APIs), enables CORS, sets session management to `STATELESS`, and permits all requests for development.

### 8. Swagger / OpenAPI
Springdoc OpenAPI 2.3.0 auto-generates interactive API documentation. Every endpoint is annotated with `@Operation`, `@ApiResponse`, and `@ExampleObject` so developers can understand and test the API directly from the browser.

---

## REST Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/compare` | Compare two quantities |
| POST | `/convert` | Convert to another unit |
| POST | `/add` | Add two quantities |
| POST | `/add-with-target-unit` | Add with explicit result unit |
| POST | `/subtract` | Subtract two quantities |
| POST | `/subtract-with-target-unit` | Subtract with explicit result unit |
| POST | `/multiply` | Multiply two quantities |
| POST | `/divide` | Divide two quantities |
| GET | `/history/operation/{op}` | Get history by operation |
| GET | `/history/type/{type}` | Get history by measurement type |
| GET | `/history/errored` | Get all errored records |
| GET | `/count/{operation}` | Count successful operations |

---

## Validation

Input validation is handled by Jakarta Bean Validation on `QuantityDTO`:
- `@NotNull` — value, unit, and measurementType cannot be null
- `@Pattern` — measurementType must match `LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit`
- `@AssertTrue` — unit must be valid for the given measurement type (e.g. `FEET` is valid for `LengthUnit` but not `VolumeUnit`)

If validation fails, `GlobalExceptionHandler` catches `MethodArgumentNotValidException` and returns `400 BAD REQUEST` with a descriptive message.

---

## Database

H2 in-memory database is used for development and testing. Every operation is saved to `quantity_measurement_entity` with full input/output details and an `is_error` flag. This enables history tracking, error auditing, and operation counts.

---

## Testing

### Unit Tests — `QuantityMeasurementControllerTest` (4 tests)
Uses `@WebMvcTest` + `@MockBean` to test the controller layer in isolation. Spring Security filters are disabled with `@AutoConfigureMockMvc(addFilters = false)`.

### Integration Tests — `QuantityMeasurementApplicationTests` (17 tests)
Uses `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate` to start a real server and make actual HTTP requests. Tests are ordered with `@TestMethodOrder` to ensure data is available for history/count tests.

### Unit Tests — `QuantityMeasurementAppTest` (53 tests)
Tests the core business logic of `Quantity.java` and unit enums directly, carried over from UC1–UC16.

### Total: 74 tests — 0 Failures, 0 Errors ✅

---

## Test Report

Maven Surefire Plugin generates an HTML test report:

```
Goals: test surefire-report:report
Report: target/site/surefire-report.html
```

---

## Live URLs

| URL | Purpose |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Swagger UI |
| `http://localhost:8080/api-docs` | OpenAPI JSON |
| `http://localhost:8080/h2-console` | H2 Database Console |
| `http://localhost:8080/actuator/health` | Health Check |

---

## Summary

UC17 brings together everything built in UC1–UC16 and wraps it in a production-ready Spring Boot application. It demonstrates clean layered architecture, RESTful API design, input validation, exception handling, database persistence, security configuration, API documentation, and comprehensive test coverage — all in a single cohesive project.

---


**Link:** [feature/UC17-SpringBackend](# QuantityMeasurementApp
## UC17 — Spring Boot REST API Migration: Quantity Measurement App

### What is UC17?

UC17 is the final and most comprehensive use case in the Quantity Measurement project. It migrates the standalone Java application built across UC1–UC16 into a fully-featured **Spring Boot REST API**. The goal is to expose all quantity measurement operations — compare, convert, add, subtract, multiply, and divide — as RESTful HTTP endpoints backed by a persistent database, secured by Spring Security, documented with Swagger, and fully tested with both unit and integration tests.

---

## Why Spring Boot?

In the earlier use cases (UC1–UC16), the Quantity Measurement logic lived entirely inside Java classes and was tested only through JUnit. While this validated the business logic, it had no way to be consumed by external clients like web apps or mobile apps.

Spring Boot solves this by wrapping the existing logic inside a production-ready web server. It provides:
- **Embedded Tomcat** — no need to deploy to an external server
- **Auto-configuration** — minimal setup for JPA, Security, Swagger
- **REST support** — expose endpoints via `@RestController`
- **Dependency Injection** — clean separation of concerns via `@Service`, `@Repository`

---

## Architecture

The application follows a standard **Layered Architecture**:

```
Client (Postman / Swagger UI)
        ↓
[ Controller Layer ]   → Receives HTTP requests, validates input
        ↓
[ Service Layer ]      → Contains business logic, unit conversion
        ↓
[ Repository Layer ]   → Persists data to H2 database via JPA
        ↓
[ Database (H2) ]      → In-memory relational database
```

---

## Key Components

### 1. JPA Entity
`QuantityMeasurementEntity` maps to the `quantity_measurement_entity` table. Every operation — whether successful or errored — is persisted to the database. This allows full history tracking.

### 2. Repository
`QuantityMeasurementRepository` extends `JpaRepository` and provides custom query methods like `findByOperation`, `findByThisMeasurementType`, `countByOperationAndIsErrorFalse`, and `findByIsErrorTrue`.

### 3. DTOs
Three DTOs handle data transfer cleanly:
- `QuantityDTO` — holds a single quantity with value, unit, and measurement type. Includes `@NotNull`, `@Pattern`, and `@AssertTrue` validation.
- `QuantityInputDTO` — wraps `thisQuantityDTO`, `thatQuantityDTO`, and optional `targetQuantityDTO` for requests.
- `QuantityMeasurementDTO` — the response object, with static factory methods `fromEntity()` and `toEntity()`.

### 4. Service Layer
`QuantityMeasurementServiceImpl` converts DTOs to `Quantity<IMeasurable>` objects using the unit enums from UC1–UC16, performs the operation, saves the result to the database, and returns a `QuantityMeasurementDTO`.

### 5. Controller
`QuantityMeasurementController` exposes 12 REST endpoints under `/api/v1/quantities`. It delegates all logic to the service layer and handles no business logic itself.

### 6. Global Exception Handler
`GlobalExceptionHandler` uses `@ControllerAdvice` to catch all exceptions in one place and return structured JSON error responses with proper HTTP status codes.

### 7. Security Config
`SecurityConfig` disables CSRF (appropriate for stateless REST APIs), enables CORS, sets session management to `STATELESS`, and permits all requests for development.

### 8. Swagger / OpenAPI
Springdoc OpenAPI 2.3.0 auto-generates interactive API documentation. Every endpoint is annotated with `@Operation`, `@ApiResponse`, and `@ExampleObject` so developers can understand and test the API directly from the browser.

---

## REST Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/compare` | Compare two quantities |
| POST | `/convert` | Convert to another unit |
| POST | `/add` | Add two quantities |
| POST | `/add-with-target-unit` | Add with explicit result unit |
| POST | `/subtract` | Subtract two quantities |
| POST | `/subtract-with-target-unit` | Subtract with explicit result unit |
| POST | `/multiply` | Multiply two quantities |
| POST | `/divide` | Divide two quantities |
| GET | `/history/operation/{op}` | Get history by operation |
| GET | `/history/type/{type}` | Get history by measurement type |
| GET | `/history/errored` | Get all errored records |
| GET | `/count/{operation}` | Count successful operations |

---

## Validation

Input validation is handled by Jakarta Bean Validation on `QuantityDTO`:
- `@NotNull` — value, unit, and measurementType cannot be null
- `@Pattern` — measurementType must match `LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit`
- `@AssertTrue` — unit must be valid for the given measurement type (e.g. `FEET` is valid for `LengthUnit` but not `VolumeUnit`)

If validation fails, `GlobalExceptionHandler` catches `MethodArgumentNotValidException` and returns `400 BAD REQUEST` with a descriptive message.

---

## Database

H2 in-memory database is used for development and testing. Every operation is saved to `quantity_measurement_entity` with full input/output details and an `is_error` flag. This enables history tracking, error auditing, and operation counts.

---

## Testing

### Unit Tests — `QuantityMeasurementControllerTest` (4 tests)
Uses `@WebMvcTest` + `@MockBean` to test the controller layer in isolation. Spring Security filters are disabled with `@AutoConfigureMockMvc(addFilters = false)`.

### Integration Tests — `QuantityMeasurementApplicationTests` (17 tests)
Uses `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate` to start a real server and make actual HTTP requests. Tests are ordered with `@TestMethodOrder` to ensure data is available for history/count tests.

### Unit Tests — `QuantityMeasurementAppTest` (53 tests)
Tests the core business logic of `Quantity.java` and unit enums directly, carried over from UC1–UC16.

### Total: 74 tests — 0 Failures, 0 Errors ✅

---

## Test Report

Maven Surefire Plugin generates an HTML test report:

```
Goals: test surefire-report:report
Report: target/site/surefire-report.html
```

---

## Live URLs

| URL | Purpose |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Swagger UI |
| `http://localhost:8080/api-docs` | OpenAPI JSON |
| `http://localhost:8080/h2-console` | H2 Database Console |
| `http://localhost:8080/actuator/health` | Health Check |

---

## Summary

UC17 brings together everything built in UC1–UC16 and wraps it in a production-ready Spring Boot application. It demonstrates clean layered architecture, RESTful API design, input validation, exception handling, database persistence, security configuration, API documentation, and comprehensive test coverage — all in a single cohesive project.

---


**Link:** [feature/UC17-SpringBackend](https://github.com/Saud0786/QuantityMeasurementApp/tree/feature/UC17-SpringBackend)
