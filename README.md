# 💼 Job Application Management Backend

> RESTful API for managing job offers, candidates, and applications built with Spring Boot 3, PostgreSQL, and JWT Security.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue?logo=postgresql)](https://www.postgresql.org/)
[![Security](https://img.shields.io/badge/Security-JWT-blueviolet?logo=jsonwebtokens)](https://jwt.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📋 Table of Contents

- [About](#about)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Security & Authentication](#security--authentication)
- [Project Status](#project-status)
- [Author](#author)

---

## 🎯 About

This project is a **professional backend REST API** designed to manage job recruitment processes. It demonstrates a robust implementation of modern Java standards, focusing on high security, clean code, and scalable architecture.

**Core Focus:**
- **Security First**: Full JWT implementation with BCrypt password hashing.
- **Clean Architecture**: Strict separation between Controller, Service, and Repository layers.
- **Reliable Migrations**: Database version control with Flyway.
- **Developer Experience**: Interactive documentation with Swagger/OpenAPI.

---

## ✨ Key Features

### ✅ Implemented
- **JWT Authentication**: Secure login and registration flow with stateless token management.
- **Role-Based Access Control (RBAC)**: Distinct permissions for `ADMIN`, `RECRUITER`, and `CANDIDATE`.
- **Job Management**: Complete CRUD for job offers (Endpoint: `/api/job-offers`).
- **Database Migrations**: Automatic schema updates and seed data via Flyway.
- **Global Error Handling**: Centralized exception management for consistent API responses.
- **CORS Configuration**: Ready for integration with modern frontend frameworks (React, Vite, etc.).
- **Password Security**: BCrypt hashing (strength 12) for all user passwords.
- **Token Expiration**: Configurable JWT expiration (default: 24 hours).

### 🔄 In Progress
- **Application Logic**: Link candidates with job offers.
- **Profile Management**: Detailed candidate CV and experience tracking.
- **Advanced Filtering**: Search and filter by location, employment type, etc.

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.4.1 |
| **Security** | Spring Security + JWT (JJWT 0.12.3) |
| **Database** | PostgreSQL 14 |
| **ORM** | Hibernate + Spring Data JPA |
| **Migration** | Flyway |
| **Mapping** | MapStruct 1.5.5 |
| **Validation** | Jakarta Bean Validation |
| **Documentation** | Swagger/OpenAPI 2.7.0 |
| **Build Tool** | Maven 3.8+ |
| **Testing** | JUnit 5 + Mockito |
| **Utilities** | Lombok |

---

## 🏗️ Architecture

The project follows a **Layered Architecture** with strict DTO mapping and security-first design:

```
┌─────────────────────────────────────────────────────┐
│                   HTTP Requests                      │
└──────────────────────┬────────────────────────────────┘
                       │
        ┌──────────────▼──────────────┐
        │   Spring Security Chain     │
        │  - CORS Configuration       │
        │  - JWT Filter               │
        │  - Authorization Checks     │
        └──────────────┬──────────────┘
                       │
        ┌──────────────▼──────────────┐
        │   Controller Layer          │
        │  - HTTP Request Handling    │
        │  - Input Validation         │
        │  - Response Formatting      │
        └──────────────┬──────────────┘
                       │
        ┌──────────────▼──────────────┐
        │   Service Layer             │
        │  - Business Logic           │
        │  - Transaction Management   │
        │  - Data Processing          │
        └──────────────┬──────────────┘
                       │
        ┌──────────────▼──────────────┐
        │   Repository Layer          │
        │  - Data Access (JPA)        │
        │  - Query Execution          │
        │  - Entity Persistence       │
        └──────────────┬──────────────┘
                       │
        ┌──────────────▼──────────────┐
        │   PostgreSQL Database       │
        │  - Data Storage             │
        │  - Schema Management        │
        │  - Constraints & Indexes    │
        └──────────────────────────────┘
```

**Key Design Patterns:**
- **DTO Pattern**: Separation between internal entities and API contracts.
- **Mapper Pattern**: MapStruct for type-safe entity-DTO transformations.
- **Repository Pattern**: Spring Data JPA abstractions for data access.
- **Exception Handling**: Global `@RestControllerAdvice` with custom exceptions.

---

## 🚀 Getting Started

### Prerequisites
- **Java 17** ([Download](https://adoptium.net/))
- **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL 14+** ([Download](https://www.postgresql.org/download/))

### Installation & Run

#### Step 1: Clone Repository
```bash
git clone https://github.com/Nikolaspc/job-application-management-backend.git
cd job-application-management-backend
```

#### Step 2: Database Setup

**Option A: Using Docker (Recommended)**
```bash
docker run --name job-app-postgres \
  -e POSTGRES_DB=job_application_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:14-alpine
```

**Option B: Using Local PostgreSQL**
```bash
createdb job_application_db
```

#### Step 3: Configure Environment
Update `src/main/resources/application.yml` with your PostgreSQL credentials:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/job_application_db
    username: postgres
    password: postgres
```

#### Step 4: Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at: **http://localhost:8080**

#### Step 5: Verify Installation
```bash
curl http://localhost:8080/api/job-offers
```

Expected response: JSON array with job offers

---

## 🔐 Security & Authentication

### Authentication Flow

1. **Register**: `POST /api/auth/register` - Create a new user account.
2. **Login**: `POST /api/auth/login` - Receive a Bearer Token (valid for 24 hours).
3. **Authorized Requests**: Include header `Authorization: Bearer <token>` in protected routes.

### Test Users (Created by Flyway Migration)

| Email | Password | Role |
|-------|----------|------|
| admin@example.com | admin123 | ADMIN |
| recruiter@example.com | recruiter123 | RECRUITER |
| candidate@example.com | candidate123 | CANDIDATE |

### Role-Based Access Matrix

| Endpoint | Method | Public | CANDIDATE | RECRUITER | ADMIN |
|----------|--------|--------|-----------|-----------|-------|
| /api/auth/register | POST | ✅ | ✅ | ✅ | ✅ |
| /api/auth/login | POST | ✅ | ✅ | ✅ | ✅ |
| /api/job-offers | GET | ✅ | ✅ | ✅ | ✅ |
| /api/job-offers/{id} | GET | ✅ | ✅ | ✅ | ✅ |
| /api/job-offers | POST | ❌ | ❌ | ✅ | ✅ |
| /api/job-offers/{id} | PUT | ❌ | ❌ | ✅ | ✅ |
| /api/job-offers/{id} | DELETE | ❌ | ❌ | ❌ | ✅ |
| /api/candidates | GET | ❌ | ✅ | ✅ | ✅ |
| /api/applications | POST | ❌ | ✅ | ❌ | ❌ |

### Security Implementation Details

- **Password Hashing**: BCrypt with strength 12 (CPU-intensive for brute-force resistance).
- **Token Generation**: HS512 algorithm with 256-bit secret key.
- **CORS Enabled**: Configured for localhost development and production domains.
- **Stateless Authentication**: JWT-based, no server-side session storage required.

---

## 📚 API Documentation

### Interactive Swagger UI

Access the full interactive documentation while the app is running:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

All endpoints are documented with:
- Request/response examples
- Parameter descriptions
- Error responses
- Try-it-out functionality

### Quick Test Examples

#### Register New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "CANDIDATE"
  }'
```

**Response (201 Created):**
```json
{
  "id": 4,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "role": "CANDIDATE",
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

#### Get Job Offers (Public)
```bash
curl http://localhost:8080/api/job-offers
```

#### Create Job Offer (Requires RECRUITER or ADMIN)
```bash
curl -X POST http://localhost:8080/api/job-offers \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Senior Java Developer",
    "description": "Expert in Spring Boot and Microservices",
    "location": "Berlin, Germany",
    "employmentType": "FULL_TIME"
  }'
```

#### Access Protected Endpoint (Authenticated Users Only)
```bash
curl -X GET http://localhost:8080/api/candidates \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 📊 Project Status

### Completion Overview

- **Security & JWT**: ████████████████████ 100% - Full implementation with BCrypt
- **Job Offers CRUD**: ████████████████████ 100% - Complete CRUD operations
- **Authentication**: ████████████████████ 100% - Register, Login, Token validation
- **Database Schema**: ████████████████████ 100% - Flyway migrations, indexes, constraints
- **Role-Based Access**: ████████████████████ 100% - @PreAuthorize on endpoints
- **API Documentation**: ████████████████████ 100% - Swagger UI fully functional
- **Applications Logic**: ██████████░░░░░░░░░░ 50% - Database schema ready, service pending
- **Candidate Profiles**: █████████░░░░░░░░░░░ 45% - Repository exists, features in progress

### What's Working
✅ Spring Boot application starts on port 8080
✅ PostgreSQL connection established (HikariCP connection pooling)
✅ Flyway migrations execute automatically (V1, V2)
✅ JWT authentication with login/register
✅ Role-based access control (RBAC)
✅ Job Offers complete CRUD
✅ Swagger UI interactive documentation
✅ Global exception handling
✅ CORS configuration
✅ Database schema with relationships and indexes
✅ Test users pre-populated

### What's In Progress
🔄 Complete Candidate and Application services
🔄 Advanced filtering and search
🔄 File upload (CV/Resume)
🔄 Email notifications
🔄 Application workflow status tracking

### Future Enhancements
⚠️ Pagination and sorting
⚠️ Advanced search filters
⚠️ File upload for documents
⚠️ Email notifications
⚠️ Application status workflow
⚠️ Docker Compose configuration
⚠️ CI/CD pipeline (GitHub Actions)
⚠️ Deployment (Railway/Render)

---

## 👤 Author

**Nikolas Pérez Cvjetkovic**

- 📧 **Email**: [n.perez.cvjetkovic@gmail.com](mailto:n.perez.cvjetkovic@gmail.com)
- 🐙 **GitHub**: [@Nikolaspc](https://github.com/Nikolaspc)
- 📍 **Location**: Germany
- 🎓 **Status**: Computer Science Engineering Student

This project was developed as part of my portfolio to demonstrate professional backend development skills for the German tech market.

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Baeldung Tutorials](https://www.baeldung.com/)
- [JWT.io Official Site](https://jwt.io/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

---

**Made with ☕ and Java in Germany**