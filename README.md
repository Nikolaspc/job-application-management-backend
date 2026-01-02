# 💼 Job Application Management Backend

> Professional RESTful API for recruitment processes. Built with Spring Boot 3, PostgreSQL, and a Security-First approach.

[![Build & Test](https://img.shields.io/github/actions/workflow/status/Nikolaspc/job-application-management-backend/maven.yml?branch=main&label=CI&logo=github)](https://github.com/Nikolaspc/job-application-management-backend/actions)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue?logo=postgresql)](https://www.postgresql.org/)

---

## 🎯 Project Overview

This system manages the recruitment lifecycle, from job publication to candidate application. It implements advanced persistence patterns and production-ready observability.

### 🚀 Key Technical Features

- **Shared Primary Key (One-to-One)**: User and Candidate entities share the same ID via `Persistable<Long>`, optimizing DB performance.
- **Automated Profiling**: Automatic creation of Candidate profiles upon registration.
- **CI/CD Integration**: Automated pipeline using GitHub Actions with a real PostgreSQL service for integration testing.
- **Database Migrations**: Schema evolution managed strictly by Flyway.

---

## 🏗️ Architecture & Stack

- **Security**: Spring Security + JWT (HS512) with Role-Based Access Control (RBAC).
- **Persistence**: Hibernate + Spring Data JPA (PostgreSQL).
- **Testing**: JUnit 5 + Mockito + **Testcontainers** for real DB integration tests.
- **Observability**: Spring Boot Actuator with exposed health and metrics endpoints.

---

## 🛠️ Getting Started

### 1. Using Docker Compose (Recommended)

The fastest way to run the entire stack (App + Database):
```bash
# Clone the repository
git clone https://github.com/Nikolaspc/job-application-management-backend.git
cd job-application-management-backend

# Start the services
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

### 2. Local Development (Native)

Ensure you have PostgreSQL 14 running and update `src/main/resources/application.yml` or set the following environment variables:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `APP_JWT_SECRET`

---

## 🔐 Security & Roles

| Role | Access Level |
|------|--------------|
| ADMIN | Full Job Offer management (CRUD) |
| CANDIDATE | Apply to jobs and manage profile |

### Auth Flow:

1. `POST /api/auth/register` (Include role: `CANDIDATE` or `ADMIN`)
2. `POST /api/auth/login` ➔ Receive JWT
3. Use header `Authorization: Bearer <token>` for protected routes

---

## 📚 API Documentation & Health

### Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: Static definition located in `/docs/openapi.yaml`

### Observability (Actuator)

- **Health Check**: `GET /actuator/health`
- **Metrics**: `GET /actuator/metrics`

---

## 👤 Author

**Nikolas Pérez Cvjetkovic**  
Computer Science Engineering Student | Based in Germany

[GitHub](https://github.com/Nikolaspc) | n.perez.cvjetkovic@gmail.com

---

*Made with ☕ and Java in Germany*