# 💼 Job Application Management Backend

> **Professional Spring Boot 3.4 REST API** for recruitment management. Built with a "Security-First" approach and Production-Ready standards for the German tech market.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![Security](https://img.shields.io/badge/Security-JWT-blueviolet?logo=jsonwebtokens)](https://jwt.io/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-blue?logo=githubactions)](https://github.com/features/actions)

---

## 🎯 Project Overview

This backend system manages the lifecycle of job recruitment: **Job Offers**, **Candidate Profiles**, and **Applications**. It focuses on technical excellence, using a strictly layered architecture and modern industry patterns.

### 🇩🇪 German Market Standards Applied:
- **Stateless Authentication**: Fully implemented JWT with secure expiration and role-based access.
- **Data Integrity**: Database migrations via **Flyway** (no `ddl-auto: update` in production).
- **Quality Assurance**: Automated CI pipeline via **GitHub Actions**.
- **Clean Code**: Strict separation of concerns (DTOs vs Entities) using **MapStruct** for high-performance mapping.

---

## 🛠️ Tech Stack & Patterns

| Category | Technology |
|----------|-----------|
| **Core** | Java 17, Spring Boot 3.4.1 |
| **Data** | PostgreSQL, Spring Data JPA, Hibernate, Flyway |
| **Security** | Spring Security 6, JWT (JJWT), BCrypt (Strength 12) |
| **Mapping** | MapStruct 1.5.5 (Type-safe DTO/Entity mapping) |
| **Docs** | OpenAPI 3 / Swagger UI |
| **DevOps** | Docker (Conceptual), GitHub Actions |

---

## 🏗️ Architecture Design

The project follows a **Strict Layered Architecture** to ensure maintainability and testability:

1.  **Controller Layer**: Handles HTTP requests, JSR-303 validation, and response wrapping.
2.  **Service Layer**: Encapsulates business logic, data validation, and `@Transactional` boundaries.
3.  **Repository Layer**: Spring Data JPA interfaces for optimized database access.
4.  **Domain Layer**: Pure JPA entities representing the relational schema with proper constraints.
5.  **DTO Layer**: Decouples internal logic from external API contracts.



---

## 🚀 Getting Started

### Prerequisites
- **Java 17+**
- **PostgreSQL 14/15/16**
- **Maven 3.8+**

### 1. Database Setup
```bash
# Create the DB in your local PostgreSQL
psql -c "CREATE DATABASE job_application_db;"
2. Configuration

The application uses src/main/resources/application.yml. Flyway will automatically run migrations located in src/main/resources/db/migration/ upon startup.

3. Run Application

Bash
./mvnw clean spring-boot:run
The API starts at: http://localhost:8080

🔐 Security & Access Matrix
All passwords are encrypted using BCrypt. Authentication is stateless via JWT Bearer Tokens.

Role	Job Offers (GET)	Job Offers (POST/PUT)	Applications (POST)	Admin Panel
GUEST	✅	❌	❌	❌
CANDIDATE	✅	❌	✅	❌
RECRUITER	✅	✅	✅	❌
ADMIN	✅	✅	✅	✅
Default Credentials (for Testing)

Admin: admin@example.com / admin123

Candidate: candidate@example.com / candidate123

📚 API Documentation
Once the app is running, visit the interactive Swagger UI to explore and test all endpoints: 👉 http://localhost:8080/swagger-ui.html

⚙️ CI/CD Pipeline
The project includes a .github/workflows/maven.yml file that triggers on every push/PR to ensure:

✅ Build: Verifies Java 17 compilation and dependency resolution.

✅ Test: Executes Unit and Integration tests.

✅ Verify: Checks for code quality and successful packaging.

💡 Technical Decisions (Interview Prep)
Why MapStruct? It generates boilerplate-free, type-safe mapping code at compile time. It is significantly faster than reflection-based alternatives like ModelMapper.

Why Flyway? In professional environments, manual DB changes are forbidden. Flyway ensures the schema is versioned, reproducible, and tracked in Git.

Why JWT? It enables a stateless backend, which is essential for modern cloud scalability and microservices readiness.

FetchType.LAZY: Configured in @ManyToOne relationships to prevent "N+1" performance issues and optimize memory usage.

👤 Author
Nikolas Pérez Cvjetkovic

🎓 Computer Science Engineering Student

📍 Based in: Germany

✉️ n.perez.cvjetkovic@gmail.com

🐙 GitHub: @Nikolaspc