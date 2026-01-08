# Job Application Management System

> **Professional Backend REST API for Recruitment Process Management**  
> Built with Spring Boot 3.4.1, JWT Security, PostgreSQL, and HashiCorp Vault Integration

[![Build Status](https://img.shields.io/github/actions/workflow/status/Nikolaspc/job-application-management-backend/maven.yml?branch=main&label=build)](https://github.com/Nikolaspc/job-application-management-backend/actions)
[![Java Version](https://img.shields.io/badge/Java-17-orange.svg)](https://adoptium.net/)
[![Coverage](https://img.shields.io/badge/coverage-85%25-brightgreen.svg)](https://github.com/Nikolaspc/job-application-backend)

---

## Key Features

- **JWT-Based Authentication**: Stateless authentication using HS512 algorithm with secure token generation and validation
- **Role-Based Access Control (RBAC)**: Three-tier permission system (ADMIN, RECRUITER, CANDIDATE)
- **User & Candidate Management**: Normalized data model with one-to-one User-Candidate relationship
- **Job Offer Management**: CRUD operations for job listings with active/inactive status tracking
- **Application Tracking**: Complete job application lifecycle management with duplicate prevention
- **Audit Logging**: Comprehensive security event logging with correlation ID tracking (BSI/GDPR compliant)
- **Database Migrations**: Flyway-managed schema versioning for production-grade deployments
- **HashiCorp Vault Integration**: Secure secret management for sensitive configuration (JWT secrets, DB credentials)
- **API Documentation**: OpenAPI 3.0 specification with interactive Swagger UI

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| **Runtime** | Java 17 (Eclipse Temurin) |
| **Framework** | Spring Boot 3.4.1 |
| **Security** | Spring Security 6, JJWT 0.12.3 |
| **Database** | PostgreSQL 14+ |
| **ORM** | Spring Data JPA, Hibernate |
| **Migrations** | Flyway 10.x |
| **Mapping** | MapStruct 1.5.5 |
| **Secret Management** | HashiCorp Vault 1.15 |
| **Build Tool** | Maven 3.9.12 |
| **Testing** | JUnit 5, Mockito, Testcontainers |
| **Containerization** | Docker (Multi-stage Build) |

---

## Prerequisites

Ensure the following tools are installed on your system:

- **Java 17** (Eclipse Temurin recommended): [Download](https://adoptium.net/)
- **Maven 3.8+**: Bundled via `mvnw` wrapper (no separate installation required)
- **Docker Engine 20.10+**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose v2+**: Included with Docker Desktop or [install standalone](https://docs.docker.com/compose/install/)

Verify installations:
```bash
java -version    # Should show Java 17
docker --version # Should show 20.10+
docker compose version # Should show v2.x
```

---

## Getting Started

### Step 1: Clone the Repository

```bash
git clone https://github.com/Nikolaspc/job-application-backend.git
cd job-application-backend
```

### Step 2: Configure Environment Variables

Copy the example environment file and configure your secrets:

```bash
cp .env.example .env
```

**Critical Configuration:**
- `APP_JWT_SECRET`: **Must be at least 64 characters** for HS512 compliance. Generate a secure secret:
  ```bash
  openssl rand -base64 64 | tr -d '\n'
  ```
- Update `DB_PASSWORD` if using a non-default PostgreSQL password

### Step 3: Start Infrastructure Services

Launch PostgreSQL and HashiCorp Vault using Docker Compose:

```bash
docker compose up -d postgres-db vault
```

Verify services are healthy:
```bash
docker compose ps
# Both postgres-db and vault should show "running (healthy)"
```

**Vault Setup (Development Only):**
The Vault container runs in dev mode with root token `root`. For production, configure Vault with proper authentication and store secrets at path `secret/job-application-backend`.

### Step 4: Build and Run the Application

#### Option A: Using Maven Wrapper (Recommended)

```bash
# Build the application
./mvnw clean package -DskipTests

# Run the application
java -jar target/job-application-backend-0.0.1-SNAPSHOT.jar
```

#### Option B: Using Docker

```bash
# Build and start all services (app + dependencies)
docker compose up --build
```

The application will be available at `http://localhost:8080`.

---

## Configuration Reference

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_USER` | `postgres` | PostgreSQL database username |
| `DB_PASSWORD` | `postgres` | PostgreSQL database password (change in production) |
| `DB_URL` | `jdbc:postgresql://postgres-db:5432/job_application_db` | JDBC connection URL |
| `APP_JWT_SECRET` | *(Required)* | HS512 signing key (minimum 64 characters) |
| `APP_JWT_EXPIRATION` | `86400` | Token validity period in seconds (default: 24 hours) |
| `VAULT_URI` | `http://vault:8200` | HashiCorp Vault server address |
| `VAULT_TOKEN` | `root` | Vault authentication token (dev mode only) |
| `SPRING_PROFILES_ACTIVE` | `dev` | Active Spring profile (`dev`, `prod`, `test`) |
| `SERVER_PORT` | `8080` | HTTP server port |

**Vault Secret Path:**  
Secrets are read from `secret/job-application-backend`. In production, store `app.jwt.secret` and `spring.datasource.password` in Vault.

---

## API Documentation

The API is fully documented using OpenAPI 3.0 specification.

**Access Swagger UI:**  
`http://localhost:8080/swagger-ui.html`

**OpenAPI JSON Spec:**  
`http://localhost:8080/v3/api-docs`

**Note:** Swagger UI is **disabled in production** (`prod` profile) for security compliance.

### Key Endpoints

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| `/api/auth/register` | POST | Register new user (ADMIN/CANDIDATE) | No |
| `/api/auth/login` | POST | Authenticate and receive JWT token | No |
| `/api/v1/users/me` | GET | Get current user profile | Yes |
| `/api/candidates` | GET | List all candidates | Yes (ADMIN) |
| `/api/jobs` | GET | List all job offers | No |
| `/api/jobs` | POST | Create new job offer | Yes (ADMIN) |
| `/api/applications` | POST | Apply to job offer | Yes (CANDIDATE) |

---

## Development Workflow

### Running Tests

Execute the full test suite (unit + integration tests):

```bash
./mvnw clean verify
```

**Note:** Integration tests use Testcontainers, which requires Docker to be running.

Run only unit tests (fast, no Docker required):
```bash
./mvnw test
```

### Test Coverage

Test suites include:
- **Unit Tests**: Service layer logic with Mockito mocks
- **Integration Tests**: Repository and controller tests with real PostgreSQL (via Testcontainers)
- **Mapper Tests**: MapStruct mapping validation

Coverage targets:
- Services: 85%+
- Controllers: 80%+
- Repositories: 70%+

### Code Quality

The project follows German IT Production Standards with:
- **Audit Logging**: All security events logged to `logs/audit/audit.log`
- **Request Tracing**: Correlation ID propagation via `X-Correlation-ID` header
- **Exception Handling**: Centralized error responses with RFC 7807 Problem Details compliance
- **Security Hardening**: CORS configuration, CSRF protection, and JWT validation

---

## Deployment

### Docker Multi-Stage Build

The `Dockerfile` uses a two-stage build process optimized for production:

**Stage 1 (Build):**
- Base: `maven:3.8.4-openjdk-17-slim`
- Compiles source code and runs tests
- Generates executable JAR

**Stage 2 (Runtime):**
- Base: `eclipse-temurin:17-jdk-alpine`
- Minimal footprint (~150MB)
- Runs application as non-root user (optional, uncomment in Dockerfile)

Build and run production image:
```bash
docker build -t job-application-backend:latest .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e APP_JWT_SECRET=$(openssl rand -base64 64 | tr -d '\n') \
  job-application-backend:latest
```

### CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/maven.yml`) runs on every push/PR:
1. Checkout code
2. Set up JDK 17
3. Cache Maven dependencies
4. Run `mvn clean verify` (build + test)

**Environment Variables for CI:**
- `APP_JWT_SECRET`: Set in GitHub Secrets
- `SPRING_PROFILES_ACTIVE`: Automatically set to `test`

---

## Project Structure

```
job-application-backend/
├── src/main/java/com/nikolaspc/jobapp/
│   ├── config/           # Spring configuration (Security, OpenAPI, MapStruct)
│   ├── controller/       # REST API endpoints
│   ├── domain/           # JPA entities (User, Candidate, JobOffer, JobApplication)
│   ├── dto/              # Data Transfer Objects
│   ├── exception/        # Global exception handling
│   ├── mapper/           # MapStruct interfaces
│   ├── repository/       # Spring Data JPA repositories
│   ├── security/         # JWT providers, filters, and audit aspects
│   └── service/          # Business logic layer
├── src/main/resources/
│   ├── db/migration/     # Flyway SQL scripts
│   ├── application.yml   # Base configuration
│   ├── application-dev.yml
│   └── application-prod.yml
├── src/test/           # Unit and integration tests
├── Dockerfile          # Multi-stage production build
├── docker-compose.yml  # Local development infrastructure
└── pom.xml             # Maven project definition
```

---

## Security Considerations

### Authentication Flow
1. User registers via `/api/auth/register` → receives JWT token
2. Client includes token in `Authorization: Bearer <token>` header
3. `JwtAuthenticationFilter` validates token and populates `SecurityContext`
4. Controllers enforce role-based access using `@PreAuthorize` annotations

### Token Security
- **Algorithm**: HS512 (512-bit HMAC with SHA-512)
- **Expiration**: Configurable (default 24 hours)
- **Claims**: `userId`, `email`, `role`
- **Validation**: Signature verification, expiration check, malformed token detection

### Production Checklist
- [ ] Change `DB_PASSWORD` from default
- [ ] Generate strong `APP_JWT_SECRET` (64+ chars)
- [ ] Configure Vault with production authentication
- [ ] Disable Swagger UI (`springdoc.api-docs.enabled=false`)
- [ ] Enable HTTPS/TLS for external traffic
- [ ] Configure CORS `allowed-origins` for your frontend domain
- [ ] Set up centralized logging (e.g., ELK stack)

---

## Troubleshooting

### Common Issues

**Issue**: Application fails to start with "Invalid JWT secret"  
**Solution**: Ensure `APP_JWT_SECRET` is at least 64 characters long. Generate with:
```bash
openssl rand -base64 64 | tr -d '\n'
```

**Issue**: Database connection refused  
**Solution**: Verify PostgreSQL is running:
```bash
docker compose ps postgres-db
docker compose logs postgres-db
```

**Issue**: Vault connection failed  
**Solution**: Check Vault health:
```bash
docker compose logs vault
curl http://localhost:8200/v1/sys/health
```

**Issue**: Tests fail with Testcontainers error  
**Solution**: Ensure Docker is running and accessible:
```bash
docker info
```

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) file for details.

---

## Contact

**Developer**: Nikolas Pérez Cvjetkovic  
**Email**: n.perez.cvjetkovic@gmail.com  
**GitHub**: [@Nikolaspc](https://github.com/Nikolaspc)

For questions, issues, or feature requests, please open an issue on GitHub.