# Job Application Management Backend

Professional Spring Boot 3.4 REST API for enterprise recruitment management. Built with **Security-First** principles and **Production-Ready** standards aligned with international tech market requirements.

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6-blue.svg)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-HS512-red.svg)](https://jwt.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14%2B-336791.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Supported-2496ED.svg)](https://www.docker.com/)
[![GitHub Actions](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF.svg)](https://github.com/features/actions)
[![Build Status](https://github.com/Nikolaspc/job-application-management-backend/actions/workflows/maven.yml/badge.svg)](https://github.com/Nikolaspc/job-application-management-backend/actions/workflows/maven.yml)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📋 Table of Contents

- [Overview](#overview)
- [Technical Architecture](#technical-architecture)
- [Technology Stack](#technology-stack)
- [Installation & Setup](#installation--setup)
- [Authentication & Authorization](#authentication--authorization)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [Author](#author)

---

## Overview

This backend system provides a comprehensive solution for managing the complete recruitment lifecycle:

- **Job Offers Management** — Create, update, and manage job postings
- **Candidate Profiles** — Maintain detailed candidate information and qualifications
- **Application Tracking** — Process and track job applications through the pipeline

The architecture emphasizes **technical excellence**, **security compliance**, and **scalability** using industry-standard patterns and proven technologies.

---

## Technical Architecture

### Design Principles

**Stateless Security with JWT HS512**  
Implemented stateless authentication using JSON Web Tokens signed with HS512. This algorithm requires a minimum 64-character key, ensuring higher entropy and resistance to brute-force attacks compared to HS256. Perfect for horizontal scalability in distributed environments.

**Database Versioning with Flyway**  
Production stability is maintained through explicit schema versioning. The `ddl-auto` setting is disabled in favor of Flyway migrations, ensuring reproducible database state across all environments and enabling safe rollbacks.

**API Decoupling via DTOs & MapStruct**  
Entities are strictly isolated from the API layer through Data Transfer Objects. MapStruct provides compile-time type-safe mapping, eliminating runtime reflection overhead and ensuring consistency across transformations.

**Quality Assurance with Testcontainers**  
Integration tests execute against real PostgreSQL instances running in Docker, guaranteeing development-production parity and catching environment-specific issues early.

### Layered Architecture

```
┌─────────────────────────────────────┐
│      Controller Layer (REST)        │
├─────────────────────────────────────┤
│      Service Layer (Business Logic) │
├─────────────────────────────────────┤
│      Repository Layer (Data Access) │
├─────────────────────────────────────┤
│    Database Layer (PostgreSQL)      │
└─────────────────────────────────────┘
```

---

## Technology Stack

| Category | Technology | Version |
|:---------|:-----------|:--------|
| **Runtime** | Java | 17+ |
| **Framework** | Spring Boot | 3.4.1 |
| **Database** | PostgreSQL | 14+ |
| **ORM** | Spring Data JPA | - |
| **Migrations** | Flyway | - |
| **Security** | Spring Security 6 | - |
| **Auth Token** | JJWT | - |
| **Password Hashing** | BCrypt | Strength 12 |
| **Mapping** | MapStruct | 1.5.5+ |
| **Utilities** | Lombok | - |
| **Documentation** | OpenAPI 3 / Swagger UI | - |
| **Testing** | Testcontainers | - |
| **Build** | Maven | 3.8+ |
| **CI/CD** | GitHub Actions | - |
| **Containerization** | Docker | - |

---

## Installation & Setup

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **PostgreSQL 14+** (local or Docker)
- **Docker** (optional, recommended)

### Option 1: Manual Setup (Local Database)

#### 1. Create Database

```sql
CREATE DATABASE job_application_db;
-- Default user: postgres
-- Default password: postgres
```

#### 2. Configure Environment Variables

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/job_application_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export APP_JWT_SECRET=YourSecretKeyWithAtLeast64CharactersForHS512SigningAlgorithm
export SPRING_PROFILES_ACTIVE=dev
```

Alternatively, edit `src/main/resources/application.yml` directly.

#### 3. Build & Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`

---

### Option 2: Docker Setup (Recommended)

```bash
docker compose up -d
```

This command orchestrates:
- PostgreSQL database
- Application container
- Network configuration

Verify the application is running:

```bash
curl -X GET http://localhost:8080/health
```

---

## Authentication & Authorization

### JWT Authentication Flow

The API uses **Bearer Token** authentication. All protected endpoints require a valid JWT in the `Authorization` header.

#### Step 1: Register Account

Create a new user account with one of three roles: `CANDIDATE`, `RECRUITER`, or `ADMIN`.

```bash
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "Nikolas",
  "lastName": "Perez",
  "email": "candidate@example.com",
  "password": "SecurePassword123!",
  "role": "CANDIDATE"
}
```

Response:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "candidate@example.com",
  "role": "CANDIDATE",
  "createdAt": "2025-01-04T10:30:00Z"
}
```

#### Step 2: Obtain Access Token

```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "candidate@example.com",
  "password": "SecurePassword123!"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjYW5kaWRhdGVAZXhhbXBsZS5jb20iLCJpYXQiOjE2NzMyMzQyMDB9...",
  "type": "Bearer",
  "expiresIn": 3600
}
```

#### Step 3: Use Token in Requests

```bash
GET /api/candidates/profile
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjYW5kaWRhdGVAZXhhbXBsZS5jb20i...
```

---

### Access Control Matrix

| Role | Job Offers (GET) | Job Offers (POST) | Applications (POST) | Admin Panel |
|:-----|:----------------:|:----------------:|:-------------------:|:-----------:|
| **GUEST** | ✅ | ❌ | ❌ | ❌ |
| **CANDIDATE** | ✅ | ❌ | ✅ | ❌ |
| **RECRUITER** | ✅ | ✅ | ✅ | ❌ |
| **ADMIN** | ✅ | ✅ | ✅ | ✅ |

---

## API Documentation

Once the application is running, access the interactive API documentation:

**📖 Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**📄 OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Project Structure

```
job-application-management-backend/
├── src/main/java/com/nikolaspc/
│   ├── controller/          # REST endpoints
│   ├── service/             # Business logic
│   ├── repository/          # Data access (JPA)
│   ├── entity/              # JPA entities
│   ├── dto/                 # Data transfer objects
│   ├── mapper/              # MapStruct mappers
│   ├── security/            # JWT & Auth config
│   ├── config/              # Spring configuration
│   └── exception/           # Custom exceptions
├── src/main/resources/
│   ├── application.yml      # Main configuration
│   ├── application-dev.yml  # Development profile
│   ├── application-prod.yml # Production profile
│   └── db/migration/        # Flyway migrations
├── src/test/java/           # Unit & integration tests
├── docker-compose.yml       # Docker orchestration
├── pom.xml                  # Maven configuration
└── README.md                # This file
```

---

## Development Workflows

### Running Tests

```bash
# Unit tests only
./mvnw test

# Integration tests (requires Docker)
./mvnw verify

# With coverage report
./mvnw clean test jacoco:report
```

### Code Quality

```bash
# Format code
./mvnw spotless:apply

# Static analysis
./mvnw checkstyle:check pmd:check
```

---

## Contributing

Contributions are welcome. Please ensure:

1. Code follows the project's style guidelines
2. All tests pass: `./mvnw clean verify`
3. New features include tests
4. Commit messages are descriptive

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) file for details.

---

## Author

**Nikolas Pérez Cvjetkovic**  
Software Developer | Based in Germany 🇩🇪

📧 [n.perez.cvjetkovic@gmail.com](mailto:n.perez.cvjetkovic@gmail.com)  
💼 [LinkedIn](https://linkedin.com) | 🐙 [GitHub](https://github.com/Nikolaspc)

---

## Support

For issues, feature requests, or questions:

- 🐛 [Open an Issue](https://github.com/Nikolaspc/job-application-management-backend/issues)
- 💬 [Start a Discussion](https://github.com/Nikolaspc/job-application-management-backend/discussions)

---

