# Job Application Management Backend

Professional Spring Boot 3.4 REST API for recruitment management. Built with a "Security-First" approach and Production-Ready standards for the German tech market.

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen?logo=spring)
![Security](https://img.shields.io/badge/Security-JWT-blueviolet)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-blue)

## Project Overview

This backend system manages the complete lifecycle of job recruitment: Job Offers, Candidate Profiles, and Applications. It focuses on technical excellence using a strictly layered architecture and modern industry patterns.

### German Market Standards Applied

- **Stateless Authentication**: Fully implemented JWT with secure expiration and role-based access control
- **Data Integrity**: Database migrations via Flyway (no `ddl-auto: update` in production)
- **Quality Assurance**: Automated CI pipeline via GitHub Actions
- **Clean Code**: Strict separation of concerns using DTOs vs Entities with MapStruct

## Tech Stack & Patterns

| Category | Technology |
|----------|-----------|
| Core | Java 17, Spring Boot 3.4.1 |
| Data | PostgreSQL, Spring Data JPA, Hibernate, Flyway |
| Security | Spring Security 6, JWT (JJWT), BCrypt (Strength 12) |
| Mapping | MapStruct 1.5.5 |
| Documentation | OpenAPI 3 / Swagger UI |
| DevOps | Docker, GitHub Actions |

## Architecture Design

The project follows a Strict Layered Architecture to ensure maintainability and testability:

1. **Controller Layer**: Handles HTTP requests, JSR-303 validation, and response wrapping
2. **Service Layer**: Encapsulates business logic and transactional boundaries
3. **Repository Layer**: Spring Data JPA interfaces for optimized database access
4. **Domain Layer**: Pure JPA entities representing the relational schema
5. **DTO Layer**: Decouples internal logic from external API contracts

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 14/15/16
- Maven 3.8 or higher

### Database Setup

Create the database in your local PostgreSQL instance:

```bash
psql -c "CREATE DATABASE job_application_db;"
```

### Configuration

The application uses `src/main/resources/application.yml`. Flyway will automatically execute migrations located in `src/main/resources/db/migration/`.

### Run Application

```bash
./mvnw clean spring-boot:run
```

The API starts at: `http://localhost:8080`

## Security & Access Matrix

All passwords are encrypted using BCrypt. Authentication is stateless via JWT Bearer Tokens.

| Role | Job Offers (GET) | Job Offers (POST/PUT) | Applications (POST) | Admin Panel |
|------|:----------------:|:--------------------:|:-------------------:|:-----------:|
| GUEST | ✓ | ✗ | ✗ | ✗ |
| CANDIDATE | ✓ | ✗ | ✓ | ✗ |
| RECRUITER | ✓ | ✓ | ✓ | ✗ |
| ADMIN | ✓ | ✓ | ✓ | ✓ |

### Default Credentials (Testing Only)

- **Admin**: admin@example.com / admin123
- **Candidate**: candidate@example.com / candidate123

## API Documentation

Once the application is running, access the interactive Swagger UI at: `http://localhost:8080/swagger-ui.html`

## CI/CD Pipeline

The project includes a `.github/workflows/maven.yml` file that triggers on every push and pull request:

- **Build**: Verifies Java 17 compilation
- **Test**: Executes unit and integration tests
- **Verify**: Checks code quality and dependencies

## Technical Decisions

**MapStruct**: Generates boilerplate-free, type-safe mapping code at compile time, outperforming reflection-based mappers like ModelMapper.

**Flyway**: In production environments, manual database changes pose significant risk. Flyway ensures the schema is versioned and reproducible across all deployment environments.

**JWT**: Enables the backend to remain stateless and scalable, facilitating seamless deployment in containerized environments such as Kubernetes.

**FetchType.LAZY**: Used in `@ManyToOne` relationships to prevent N+1 query problems and unnecessary data loading.

## Author

**Nikolas Pérez Cvjetkovic**  
Computer Science Student  
Based in: Germany  
Email: n.perez.cvjetkovic@gmail.com