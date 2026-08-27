# 🏦 Mini Banking System

## 📌 About the Project

The **Mini Banking System** is a backend banking application developed using **Java, Spring Boot, PostgreSQL, JDBC, and Maven**.

The project is designed to simulate the core operations of a banking system through REST APIs. It provides functionality for managing customers and bank accounts, performing deposits and withdrawals, transferring money between accounts, and maintaining transaction history.

A major focus of the project is **safe and reliable financial transaction processing**. The application uses database transactions, commit and rollback mechanisms, and concurrency handling to maintain data consistency during banking operations.

The project follows a **layered architecture** that separates the API, business logic, and database access responsibilities.

---

# ✨ Features

### 👤 Customer Management

- Create new customers
- Retrieve customer information
- Store customer details in PostgreSQL
- Validate customer information

### 🏦 Account Management

- Create bank accounts
- Retrieve account information
- Support Savings Accounts
- Support Current Accounts
- Maintain account balances
- Associate accounts with customers

### 💰 Deposit

- Deposit money into an account
- Validate deposit amount
- Update account balance
- Persist updated balance in PostgreSQL

### 💸 Withdrawal

- Withdraw money from an account
- Validate withdrawal amount
- Check available balance
- Prevent withdrawal when sufficient balance is unavailable
- Update account balance

### 🔄 Fund Transfer

- Transfer money between two accounts
- Validate source and destination accounts
- Validate transfer amount
- Check available balance
- Debit the source account
- Credit the destination account
- Record the transaction
- Commit successful transfers
- Roll back failed transfers

### 📜 Transaction History

- Store transaction records
- Retrieve transaction history
- Store source and destination accounts
- Store transaction amount
- Store transaction type
- Store transaction timestamp

### 🔒 Transaction Safety

- Database transaction management
- Commit and rollback support
- Protection against partially completed transfers
- Database consistency during financial operations

### 🧵 Concurrency Handling

- Support concurrent transfer operations
- Use database-level transaction management
- Use row-level locking where required
- Prevent conflicting account balance updates
- Test concurrent transactions

### 🧪 Automated Testing

- JUnit-based testing
- Transaction service testing
- Transfer testing
- Failure scenario testing
- Concurrent transfer testing

### 📖 API Documentation

- RESTful APIs
- Swagger/OpenAPI documentation
- Interactive API testing

---

# 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 25 | Core programming language |
| Spring Boot 4.1 | Backend framework |
| Spring Web | REST API development |
| Spring Validation | Request validation |
| PostgreSQL | Relational database |
| JDBC | Database connectivity |
| Maven | Build and dependency management |
| JUnit | Automated testing |
| Swagger / OpenAPI | API documentation |
| Apache Tomcat | Embedded web server |
| SLF4J | Logging |
| Logback | Logging implementation |
| IntelliJ IDEA | Development environment |

---

## ⚙️ Setting Up the Project

Follow the steps below to set up and run the **Mini Banking System** on your local machine.

### 1. Prerequisites

Before setting up the project, make sure the following software is installed:

- **Java 25**
- **Maven**
- **PostgreSQL**
- **Git**
- **IntelliJ IDEA** (recommended)

Verify Java installation:

```bash  
java -version
