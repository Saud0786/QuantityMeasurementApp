# 📘 BridgeLabz Training – MySQL JDBC Practice  
### Week 8 - Day 1: Database SQL 

This repository contains hands-on practice programs for **Java Database Connectivity (JDBC)** using **MySQL**. The focus is on building real-world, database-driven console applications with proper handling of CRUD operations, transactions, and relational data.

---

## 🧠 Core Concepts Covered

- JDBC architecture and workflow  
- MySQL database connectivity  
- CRUD operations using `PreparedStatement`  
- Transaction management (`commit`, `rollback`)  
- Exception handling in database operations  
- Search and filtering using SQL queries  
- Mapping database records to Java objects  



## 🛠️ Practice Exercises Implemented

### 👨‍💼 Exercise 1: Employee Management System (CRUD)
A console-based application that supports:

- ➕ Add new employees  
- 📋 View all employee records  
- ✏️ Update employee salary  
- ❌ Delete employee details  
- 🔍 Search employee by name  

**Focus Areas:**
- CRUD operations  
- Prepared statements  
- ResultSet processing  



### 🏦 Exercise 2: Banking System – Transaction Management
A transaction-safe banking system implementing:

- 💸 Money transfer between accounts using transactions  
- 💰 Balance checking  
- 🧾 Transaction history tracking  
- 🔄 Proper rollback on failure  

**Focus Areas:**
- ACID properties  
- Transaction management  
- Error handling and consistency  



### 📚 Exercise 3: Library Management System (Advanced)
A database-driven library system supporting:

- 📖 Book inventory management  
- 🎓 Student borrowing records  
- ⏰ Fine calculation for late returns  
- 🔎 Search functionality with multiple filters  

**Focus Areas:**
- Relational data handling  
- SQL joins  
- Real-world query design  


## ⚙️ Tech Stack

- **Language:** Java  
- **Database:** MySQL  
- **Connectivity:** JDBC  
- **Tools:** MySQL Workbench / CLI, IDE (IntelliJ / Eclipse)

---


**Code Link:** [Database SQL & Questions](https://github.com/Saud0786/BridgeLabz-Training/tree/dbms-jdbc-practice/dbms-jdbc-practice/gcr-codebase/MySqlConcepts/src/com/mysqlpracticeexercise)


---


## Section - B
## Scenarion Based Question

# 🏥 Hospital Management System

## 📌 Overview
This project implements a Hospital Management System (HMS) that supports patient management, doctor management, appointment scheduling, medical records, billing, and system administration.  
The system is designed using relational database concepts and JDBC-based backend operations to ensure data integrity, security, and performance.

---

## 👥 Actors
- Receptionist – Handles patient registration, appointments, billing  
- Doctor – Manages visits, diagnoses, prescriptions  
- Administrator – Manages doctors, specialties, reports, and system audits  
- System – Performs scheduled tasks like database backups  

---

## 1️⃣ Patient Management

### UC-1.1: Register New Patient
**Actor:** Receptionist  
**Flow:**  
- Enter patient details (name, DOB, contact, address, blood group)  
- Validate uniqueness by phone/email  
- Generate patient ID (auto-increment)  
- Insert record into `patients` table  

### UC-1.2: Update Patient Information
**Actor:** Receptionist  
**Flow:**  
- Search patient by ID/phone  
- Display current details  
- Update fields using `UPDATE` with `WHERE patient_id = ?`  

### UC-1.3: Search Patient Records
**Actor:** Receptionist / Doctor  
**Flow:**  
- Search by name using `LIKE`  
- Search by ID/phone using exact match  
- Display results using ResultSet  

### UC-1.4: View Patient Visit History
**Actor:** Doctor / Receptionist  
**Flow:**  
- Execute JOIN query between `appointments` and `visits`  
- Filter by patient ID  
- Display chronological visit history  

---

## 2️⃣ Doctor Management

### UC-2.1: Add Doctor Profile
**Actor:** Administrator  
**Flow:**  
- Input doctor details (name, specialization, contact, fee)  
- Insert into `doctors` table  
- Reference `specialties` using foreign key  

### UC-2.2: Assign/Update Doctor Specialty
**Actor:** Administrator  
**Flow:**  
- Display specialties from lookup table  
- Update `specialty_id` in `doctors`  
- Use transaction for referential integrity  

### UC-2.3: View Doctors by Specialty
**Actor:** Receptionist  
**Flow:**  
- JOIN `doctors` and `specialties`  
- Filter by specialty name  
- Display doctors and schedules  

### UC-2.4: Deactivate Doctor Profile
**Actor:** Administrator  
**Flow:**  
- Soft delete: set `is_active = false`  
- Check for future appointments before deactivation  

---

## 3️⃣ Appointment Scheduling

### UC-3.1: Book New Appointment
**Actor:** Receptionist  
**Flow:**  
- Select patient and doctor  
- Check availability  
- Insert appointment with status `SCHEDULED`  
- Use PreparedStatement to prevent SQL injection  

### UC-3.2: Check Doctor Availability
**Actor:** Receptionist  
**Flow:**  
- Query appointments by doctor and date  
- Use `GROUP BY` and `COUNT` for slot capacity  

### UC-3.3: Cancel Appointment
**Actor:** Receptionist / Patient  
**Flow:**  
- Update appointment status to `CANCELLED`  
- Log cancellation in `appointment_audit`  
- Use transaction  

### UC-3.4: Reschedule Appointment
**Actor:** Receptionist  
**Flow:**  
- Verify new slot availability  
- Update appointment date/time  
- Use transaction with ROLLBACK on conflict  

### UC-3.5: View Daily Appointment Schedule
**Actor:** Doctor / Receptionist  
**Flow:**  
- JOIN `appointments`, `patients`, `doctors`  
- Filter by date  
- Order by appointment time  

---

## 4️⃣ Visit Management & Medical Records

### UC-4.1: Record Patient Visit
**Actor:** Doctor  
**Flow:**  
- Insert visit record (diagnosis, notes, prescription)  
- Update appointment status to `COMPLETED`  
- Commit both in one transaction  

### UC-4.2: View Patient Medical History
**Actor:** Doctor  
**Flow:**  
- JOIN `visits`, `prescriptions`, `appointments`  
- Filter by patient  
- Order by visit date DESC  

### UC-4.3: Add Prescription Details
**Actor:** Doctor  
**Flow:**  
- Insert multiple prescription records  
- Use batch inserts for efficiency  

---

## 5️⃣ Billing & Payments

### UC-5.1: Generate Bill for Visit
**Actor:** Receptionist  
**Flow:**  
- Calculate total charges  
- Insert into `bills` table  
- Use `SUM()` for itemized billing  

### UC-5.2: Record Payment
**Actor:** Receptionist  
**Flow:**  
- Update bill status to `PAID`  
- Insert record into `payment_transactions`  
- Use transaction  

### UC-5.3: View Outstanding Bills
**Actor:** Receptionist / Administrator  
**Flow:**  
- Select unpaid bills  
- JOIN with patients  
- Use `SUM()` and `COUNT()` grouped by patient  

### UC-5.4: Generate Revenue Report
**Actor:** Administrator  
**Flow:**  
- Aggregate revenue by doctor/specialty/date  
- Use `BETWEEN`, `GROUP BY`, `HAVING`  

---

## 6️⃣ System Administration

### UC-6.1: Manage Specialty Lookup
**Actor:** Administrator  
**Flow:**  
- Perform CRUD on `specialties` table  
- Check foreign key constraints before DELETE  

### UC-6.2: Database Backup Trigger
**Actor:** System  
**Flow:**  
- Scheduled export of critical tables  
- Use DatabaseMetaData for schema validation  

### UC-6.3: View System Audit Logs
**Actor:** Administrator  
**Flow:**  
- Query `audit_log` table  
- Filter by user, table, timestamp  

---

## 🧠 Key Database Concepts Used
- Primary & Foreign Keys  
- Auto-increment  
- INNER / LEFT JOIN  
- Transactions (COMMIT / ROLLBACK)  
- PreparedStatements (SQL Injection prevention)  
- Connection Pooling  
- Indexing  
- Triggers for audit logs  
- Aggregate functions (`SUM`, `COUNT`, `AVG`)  
- Subqueries & correlated queries  
- Batch operations  
- ResultSet navigation & metadata  


**Cdde Link:** [SQL_Scenario_Questions](https://github.com/Saud0786/BridgeLabz-Training/tree/dbms-jdbc-practice/dbms-jdbc-practice/scenario-codebase/MySqlScenarioBased/src/com)


