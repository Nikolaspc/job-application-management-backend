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

## 📋 Inhaltsverzeichnis

- [Überblick](#überblick)
- [Technische Architektur](#technische-architektur)
- [Technologie-Stack](#technologie-stack)
- [Installation & Setup](#installation--setup)
- [Authentifizierung & Autorisierung](#authentifizierung--autorisierung)
- [API-Dokumentation](#api-dokumentation)
- [Projektstruktur](#projektstruktur)
- [Datenbank-Schema](#datenbank-schema)
- [Konfiguration](#konfiguration)
- [Deployment](#deployment)
- [Fehlerbehebung](#fehlerbehebung)
- [Best Practices](#best-practices)
- [Mitwirkende](#mitwirkende)
- [Autor](#autor)

---

## Überblick

Dieses Backend-System bietet eine umfassende Lösung für die Verwaltung des kompletten Rekrutierungszyklus:

- **Job-Angebote-Management** — Erstellen, aktualisieren und verwalten Sie Stellenausschreibungen
- **Kandidaten-Profile** — Verwalten Sie detaillierte Kandidateninformationen und Qualifikationen
- **Bewerbungs-Tracking** — Verfolgen Sie Bewerbungen durch den gesamten Prozess

Die Architektur betont **technische Exzellenz**, **Sicherheitskonformität** und **Skalierbarkeit** unter Verwendung bewährter Industriemuster und moderner Technologien.

---

## Technische Architektur

### Design-Prinzipien

**Zustandslose Sicherheit mit JWT HS512**  
Implementierte zustandslose Authentifizierung mit JSON Web Tokens, signiert mit HS512. Dieser Algorithmus erfordert einen Mindesschlüssel von 64 Zeichen und bietet höhere Entropie und Widerstandsfähigkeit gegen Brute-Force-Angriffe im Vergleich zu HS256. Perfekt für horizontale Skalierbarkeit in verteilten Umgebungen.

**Datenbank-Versionierung mit Flyway**  
Produktionsstabilität wird durch explizite Schema-Versionierung gewährleistet. Die `ddl-auto`-Einstellung ist deaktiviert zugunsten von Flyway-Migrationen, was einen reproduzierbaren Datenbankzustand über alle Umgebungen hinweg sicherstellt und sichere Rollbacks ermöglicht.

**API-Entkopplung via DTOs & MapStruct**  
Entitäten sind streng von der API-Schicht durch Datenübertragungsobjekte isoliert. MapStruct bietet typsichere Compile-Zeit-Mapping, eliminiert Runtime-Reflection-Overhead und stellt Konsistenz in Transformationen sicher.

**Qualitätssicherung mit Testcontainers**  
Integrationstests werden gegen echte PostgreSQL-Instanzen in Docker ausgeführt, was Development-Production Parity garantiert und umgebungsspezifische Probleme frühzeitig erkennt.

### Geschichtete Architektur

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

## Technologie-Stack

| Kategorie | Technologie | Version |
|:----------|:-----------|:--------|
| **Runtime** | Java | 17+ |
| **Framework** | Spring Boot | 3.4.1 |
| **Datenbank** | PostgreSQL | 14+ |
| **ORM** | Spring Data JPA | — |
| **Migrationen** | Flyway | — |
| **Sicherheit** | Spring Security 6 | — |
| **Auth Token** | JJWT | — |
| **Password Hashing** | BCrypt | Strength 12 |
| **Mapping** | MapStruct | 1.5.5+ |
| **Utilities** | Lombok | — |
| **Dokumentation** | OpenAPI 3 / Swagger UI | — |
| **Testing** | Testcontainers | — |
| **Build** | Maven | 3.8+ |
| **CI/CD** | GitHub Actions | — |
| **Containerisierung** | Docker | — |

---

## Installation & Setup

### Voraussetzungen

- **Java 17** oder höher
- **Maven 3.8+**
- **PostgreSQL 14+** (lokal oder Docker)
- **Docker** (optional, empfohlen)

### Option 1: Manuelle Einrichtung (Lokale Datenbank)

#### 1. Datenbank erstellen

```sql
CREATE DATABASE job_application_db;
-- Standard-Benutzer: postgres
-- Standard-Passwort: postgres
```

#### 2. Umgebungsvariablen konfigurieren

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/job_application_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export APP_JWT_SECRET=YourSecretKeyWithAtLeast64CharactersForHS512SigningAlgorithm
export SPRING_PROFILES_ACTIVE=dev
```

Alternativ können Sie `src/main/resources/application.yml` direkt bearbeiten.

#### 3. Bauen & Ausführen

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Die Anwendung startet auf `http://localhost:8080`

---

### Option 2: Docker Setup (Empfohlen)

```bash
docker compose up -d
```

Dieser Befehl orchestriert:
- PostgreSQL-Datenbank
- Anwendungs-Container
- Netzwerk-Konfiguration

Überprüfen Sie, dass die Anwendung läuft:

```bash
curl -X GET http://localhost:8080/health
```

---

## Authentifizierung & Autorisierung

### JWT-Authentifizierungs-Workflow

Die API verwendet **Bearer Token**-Authentifizierung. Alle geschützten Endpunkte erfordern ein gültiges JWT im `Authorization`-Header.

#### Schritt 1: Konto registrieren

Erstellen Sie ein neues Benutzerkonto mit einer von drei Rollen: `CANDIDATE`, `RECRUITER` oder `ADMIN`.

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

Antwort:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "candidate@example.com",
  "role": "CANDIDATE",
  "createdAt": "2025-01-04T10:30:00Z"
}
```

#### Schritt 2: Zugangstoken abrufen

```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "candidate@example.com",
  "password": "SecurePassword123!"
}
```

Antwort:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjYW5kaWRhdGVAZXhhbXBsZS5jb20iLCJpYXQiOjE2NzMyMzQyMDB9...",
  "type": "Bearer",
  "expiresIn": 3600
}
```

#### Schritt 3: Token in Anfragen verwenden

```bash
GET /api/candidates/profile
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjYW5kaWRhdGVAZXhhbXBsZS5jb20i...
```

---

### Zugriffskontroll-Matrix

| Rolle | Job-Angebote (GET) | Job-Angebote (POST) | Bewerbungen (POST) | Admin-Panel |
|:-----|:----------------:|:----------------:|:-------------------:|:-----------:|
| **GAST** | ✅ | ❌ | ❌ | ❌ |
| **KANDIDAT** | ✅ | ❌ | ✅ | ❌ |
| **RECRUITER** | ✅ | ✅ | ✅ | ❌ |
| **ADMIN** | ✅ | ✅ | ✅ | ✅ |

---

## API-Dokumentation

Sobald die Anwendung läuft, greifen Sie auf die interaktive API-Dokumentation zu:

**📖 Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**📄 OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Projektstruktur

```
job-application-management-backend/
├── src/
│   ├── main/
│   │   ├── java/com/nikolaspc/jobapp/
│   │   │   ├── config/
│   │   │   │   ├── MapperConfig.java           # MapStruct Konfiguration
│   │   │   │   ├── OpenApiConfig.java          # Swagger/OpenAPI Setup
│   │   │   │   └── SecurityConfig.java         # Spring Security & CORS
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java         # Login & Register Endpunkte
│   │   │   │   ├── CandidateController.java    # Kandidaten CRUD
│   │   │   │   ├── JobApplicationController.java
│   │   │   │   └── JobOfferController.java     # Job CRUD
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── User.java                   # Basis-User-Entity
│   │   │   │   ├── Candidate.java              # Kandidaten-Profil
│   │   │   │   ├── JobOffer.java               # Job-Angebot-Entity
│   │   │   │   ├── JobApplication.java         # Bewerbungs-Tracking
│   │   │   │   └── UserRole.java               # Enum: ADMIN, RECRUITER, CANDIDATE
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── joboffer/
│   │   │   │   │   ├── JobOfferRequestDTO.java
│   │   │   │   │   └── JobOfferResponseDTO.java
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── AuthResponse.java           # JWT Antwort
│   │   │   │   ├── CandidateDTO.java
│   │   │   │   ├── JobApplicationDTO.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   └── UserDto.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── ApiError.java               # Error Record Format
│   │   │   │   ├── ApiException.java           # Basis-Exception
│   │   │   │   ├── BadRequestException.java    # 400 Fehler
│   │   │   │   ├── JwtException.java           # JWT Validierungsfehler
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── UserAlreadyExistsException.java
│   │   │   │   ├── ErrorResponse.java          # Error Response Format
│   │   │   │   └── GlobalExceptionHandler.java # @RestControllerAdvice
│   │   │   │
│   │   │   ├── mapper/
│   │   │   │   ├── CandidateMapper.java        # MapStruct Interface
│   │   │   │   ├── JobApplicationMapper.java
│   │   │   │   └── JobOfferMapper.java         # Auto Mapping Entity ↔ DTO
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java         # findByEmail, existsByEmail
│   │   │   │   ├── CandidateRepository.java    # Benutzerdefinierte Abfragen
│   │   │   │   ├── JobOfferRepository.java     # findByActiveTrue()
│   │   │   │   └── JobApplicationRepository.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java       # Token-Generierung & Validierung (HS512)
│   │   │   │   ├── JwtAuthenticationFilter.java # Request-Interceptor
│   │   │   │   ├── JwtAuthenticationEntryPoint.java # 401 Handler
│   │   │   │   └── JwtUserDetails.java         # Token Payload Holder
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java            # Register & Login Logik
│   │   │   │   ├── CandidateService.java       # CRUD Interface
│   │   │   │   ├── JobApplicationService.java
│   │   │   │   ├── JobOfferService.java
│   │   │   │   └── impl/
│   │   │   │       ├── CandidateServiceImpl.java
│   │   │   │       ├── JobApplicationServiceImpl.java
│   │   │   │       └── JobOfferServiceImpl.java
│   │   │   │
│   │   │   └── JobApplicationBackendApplication.java # @SpringBootApplication
│   │   │
│   │   └── resources/
│   │       ├── application.yml                 # Haupt-Konfiguration
│   │       ├── application-dev.yml             # Development Einstellungen
│   │       ├── application-prod.yml            # Production Einstellungen
│   │       └── db/migration/                   # Flyway SQL Migrationen
│   │
│   └── test/
│       └── java/com/nikolaspc/jobapp/
│           ├── controller/
│           │   └── CandidateControllerIntegrationTest.java
│           │
│           ├── exception/
│           │   ├── GlobalExceptionHandlerTest.java
│           │   ├── ExceptionTestController.java
│           │   ├── TestController.java
│           │   └── ResourceNotFoundException.java
│           │
│           ├── mapper/
│           │   └── JobOfferMapperTest.java
│           │
│           ├── repository/
│           │   └── CandidateRepositoryIT.java (Integration Test)
│           │
│           ├── service/
│           │   ├── CandidateServiceImplTest.java
│           │   ├── JobApplicationServiceImplTest.java (15+ Test-Cases)
│           │   └── JobOfferServiceImplTest.java
│           │
│           └── AbstractTestContainers.java     # Basis-Klasse für IT Tests
│
├── docker-compose.yml                          # PostgreSQL + App Orchestration
├── pom.xml                                      # Maven Konfig
├── .gitignore
├── README.md                                    # Diese Datei
└── .github/
    └── workflows/
        └── maven.yml                           # CI/CD Pipeline
```

### Schicht-Verantwortlichkeiten

**Controller-Schicht**
- Behandelt HTTP-Anfragen/Antworten
- Validiert Eingabe mit `@Valid`
- Delegiert an Services
- Gibt angemessene HTTP-Statuscodes zurück

**Service-Schicht**
- Business-Logik & Validierung
- Transaktionsverwaltung (`@Transactional`)
- Fehlerbehandlung
- Integration zwischen Repositories

**Repository-Schicht**
- Spring Data JPA Interfaces
- Datenbankabfragen (auto-implementiert oder benutzerdefiniert)
- Beispiel: `findByEmail()`, `findByActiveTrue()`

**Ausnahmeverarbeitung**
- Zentralisiert via `GlobalExceptionHandler`
- Benutzerdefinierte Exceptions für spezifische Szenarios
- Konsistentes Error Response Format

**Sicherheit**
- JWT HS512 Token-Generierung
- Zustandslose Authentifizierungs-Filter
- Rollenbasierte Zugriffskontrolle

---

## Datenbank-Schema

### Kern-Tabellen

**users**
```sql
id (PK, SERIAL)
first_name VARCHAR(100)
last_name VARCHAR(100)
email VARCHAR(255) UNIQUE
password VARCHAR(255) -- BCrypt gehashed
role ENUM (ADMIN, RECRUITER, CANDIDATE)
active BOOLEAN DEFAULT true
created_at TIMESTAMP DEFAULT NOW()
updated_at TIMESTAMP
```

**candidates**
```sql
id (PK, FK → users.id)
first_name VARCHAR(100)
last_name VARCHAR(100)
email VARCHAR(255) UNIQUE
date_of_birth DATE
```

**job_offers**
```sql
id (PK, SERIAL)
title VARCHAR(100)
description TEXT
location VARCHAR(255)
employment_type VARCHAR(50) -- FULL_TIME, PART_TIME, CONTRACT
active BOOLEAN DEFAULT true
created_at TIMESTAMP DEFAULT NOW()
updated_at TIMESTAMP
```

**job_applications**
```sql
id (PK, SERIAL)
candidate_id (FK → candidates.id)
job_offer_id (FK → job_offers.id)
status VARCHAR(50) -- PENDING, REVIEWED, ACCEPTED, REJECTED
UNIQUE(candidate_id, job_offer_id) -- Duplikate verhindern
```

---

## Konfiguration

### Umgebungsvariablen

```bash
# Datenbank
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/job_application_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# JWT Sicherheit
APP_JWT_SECRET=YourSecretKeyWithAtLeast64CharactersForHS512SigningAlgorithm
APP_JWT_EXPIRATION=3600  # Sekunden (1 Stunde)

# Profile
SPRING_PROFILES_ACTIVE=dev  # dev, prod, test
```

### application.yml Struktur

```yaml
spring:
  application:
    name: job-application-backend
  
  jpa:
    hibernate:
      ddl-auto: validate  # Niemals auto-create in Production
    database-platform: org.hibernate.dialect.PostgreSQLDialect
  
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
  
  flyway:
    enabled: true
    locations: classpath:db/migration

app:
  jwt:
    secret: ${APP_JWT_SECRET}
    expiration: ${APP_JWT_EXPIRATION:3600}
```

---

## Komplette API-Referenz

### Authentifizierungs-Endpunkte

| Methode | Endpunkt | Rolle | Beschreibung |
|---------|----------|-------|-------------|
| POST | `/api/auth/register` | Public | Neues Benutzerkonto erstellen |
| POST | `/api/auth/login` | Public | Authentifizierung & JWT erhalten |

### Kandidaten-Endpunkte

| Methode | Endpunkt | Rolle | Beschreibung |
|---------|----------|-------|-------------|
| GET | `/api/candidates` | RECRUITER, ADMIN | Alle Kandidaten auflisten |
| GET | `/api/candidates/{id}` | RECRUITER, ADMIN | Kandidatendetails abrufen |
| POST | `/api/candidates` | RECRUITER, ADMIN | Kandidaten erstellen |
| PUT | `/api/candidates/{id}` | RECRUITER, ADMIN | Kandidaten aktualisieren |
| DELETE | `/api/candidates/{id}` | ADMIN | Kandidaten löschen |

### Job-Angebots-Endpunkte

| Methode | Endpunkt | Rolle | Beschreibung |
|---------|----------|-------|-------------|
| GET | `/api/jobs` | Public | Alle Job-Angebote auflisten |
| GET | `/api/jobs/{id}` | Public | Job-Angebot-Details abrufen |
| POST | `/api/jobs` | RECRUITER, ADMIN | Job-Angebot erstellen |
| PUT | `/api/jobs/{id}` | RECRUITER, ADMIN | Job-Angebot aktualisieren |
| DELETE | `/api/jobs/{id}` | ADMIN | Job-Angebot löschen |

### Bewerbungs-Endpunkte

| Methode | Endpunkt | Rolle | Beschreibung |
|---------|----------|-------|-------------|
| GET | `/api/applications` | RECRUITER, ADMIN | Alle Bewerbungen auflisten |
| GET | `/api/applications/{id}` | RECRUITER, ADMIN | Bewerbungsdetails abrufen |
| POST | `/api/applications` | CANDIDATE, RECRUITER | Bewerbung erstellen |

### Gesundheit & Überwachung

| Endpunkt | Zugriff | Zweck |
|----------|--------|---------|
| `/swagger-ui.html` | Public | Interaktive API-Dokumentation |
| `/v3/api-docs` | Public | OpenAPI JSON Schema |
| `/actuator/health` | Public | Anwendungs-Gesundheitsstatus |
| `/actuator/**` | ADMIN | Erweiterte Metriken & Diagnostik |

---

## Entwicklungs-Workflows

### Tests ausführen

```bash
# Nur Unit Tests
./mvnw test

# Integration Tests (erfordert Docker)
./mvnw verify

# Mit Coverage-Bericht
./mvnw clean test jacoco:report
```

### Code-Qualität

```bash
# Code formatieren
./mvnw spotless:apply

# Statische Analyse
./mvnw checkstyle:check pmd:check
```

---

## Deployment

### Docker Build & Deploy

```bash
# Docker Image bauen
docker build -t job-app:1.0 .

# Container mit PostgreSQL ausführen
docker run --name job-app \
  --link postgres:db \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/job_application_db \
  -e APP_JWT_SECRET=your-secret-key \
  -p 8080:8080 \
  job-app:1.0
```

### Production Checkliste

- [ ] Setzen Sie `SPRING_PROFILES_ACTIVE=prod`
- [ ] Konfigurieren Sie starken `APP_JWT_SECRET` (64+ Zeichen)
- [ ] Aktivieren Sie Flyway-Migrationen (`spring.flyway.enabled=true`)
- [ ] Setzen Sie `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Konfigurieren Sie CORS für Production Domain
- [ ] Verwenden Sie HTTPS/TLS in Reverse Proxy
- [ ] Richten Sie Datenbank-Backups ein
- [ ] Aktivieren Sie Anwendungs-Überwachung (Actuator)
- [ ] Konfigurieren Sie angemessene Logging-Level

### CI/CD Pipeline (GitHub Actions)

Befindet sich in `.github/workflows/maven.yml`

**Auslöser:**
- Bei jedem `push` zum `main` Branch
- Bei `pull_request` Erstellung

**Schritte:**
1. Mit Maven bauen (`mvn clean install`)
2. Unit Tests ausführen
3. Integration Tests ausführen (mit Testcontainers)
4. Test-Bericht generieren

Status anschauen: [![Java CI with Maven](https://github.com/Nikolaspc/job-application-management-backend/actions/workflows/maven.yml/badge.svg)](https://github.com/Nikolaspc/job-application-management-backend/actions/workflows/maven.yml)

---

## Fehlerbehebung

### Port bereits in Verwendung

```bash
# Prozess auf Port 8080 finden und beenden
lsof -i :8080
kill -9 <PID>

# Oder verwenden Sie einen anderen Port
./mvnw spring-boot:run -Dspring-boot.run.arguments='--server.port=8081'
```

### Datenbankverbindung fehlgeschlagen

```bash
# Überprüfen Sie, ob PostgreSQL läuft
docker ps | grep postgres

# Überprüfen Sie das Verbindungszeichenfolgen-Format
# ✓ Richtig: jdbc:postgresql://localhost:5432/job_application_db
# ✗ Falsch:  postgres://localhost:5432/job_application_db
```

### JWT Token abgelaufen oder ungültig

**Fehler-Antwort:**
```json
{
  "timestamp": "2025-01-04T12:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Expired JWT token",
  "path": "/api/candidates"
}
```

**Lösung:**
- Token über `/api/auth/login` neu generieren
- Überprüfen Sie die `app.jwt.expiration` Einstellung
- Überprüfen Sie, dass JWT Secret zwischen Generierung und Validierung übereinstimmt

### Validierungsfehler

**Fehler-Antwort:**
```json
{
  "status": 400,
  "message": "Validation Failed",
  "timestamp": "2025-01-04T12:00:00",
  "path": "/api/candidates",
  "errors": {
    "email": "Email should be valid",
    "dateOfBirth": "Date of birth must be in the past"
  }
}
```

**Häufige Probleme:**
- Email-Format: muss `@` Domain enthalten
- Datum-Format: muss `yyyy-MM-dd` sein
- Felder mit `@NotBlank`: dürfen nicht leer sein
- Altersanforderung: Minimum 18 Jahre

---

## Best Practices

### Sicherheits-Richtlinien

1. **Niemals Geheimnisse committen** — Verwenden Sie Umgebungsvariablen
   ```bash
   # ✓ Gut
   APP_JWT_SECRET=${RANDOM_64_CHAR_KEY}
   
   # ✗ Schlecht
   app.jwt.secret: ThisIsMySecretKey123
   ```

2. **Passwort-Stärke** — Erzwingen Sie Mindestens 8 Zeichen, Sonderzeichen

3. **CORS-Konfiguration** — Einschränken auf bekannte Frontend-Origins
   ```java
   // In SecurityConfig.java
   config.setAllowedOrigins(Arrays.asList("https://yourdomain.com"));
   ```

4. **Rate Limiting** — Implementieren Sie zum Schutz vor Brute-Force-Angriffen

### Leistungs-Optimierung

1. **Lazy Loading** — Relationships verwenden `FetchType.LAZY`
2. **Pagination** — Fügen Sie `Pageable` für große Datensätze hinzu
3. **Caching** — Erwägen Sie `@Cacheable` für häufig zugegriffene Daten
4. **Datenbank-Indexierung** — Fügen Sie Indizes für häufig abgefragte Spalten hinzu

### Code-Qualitäts-Standards

1. **REST-Konventionen befolgen** — Resource-orientierte Endpunkte
2. **Konsistente Benennung** — camelCase für Java, snake_case für DB
3. **Umfassendes Logging** — Verwenden Sie `@Slf4j` zum Debugging
4. **Transaktions-Umfang** — Halten Sie `@Transactional` fokussiert

---

## Technischer Fahrplan & Zukünftige Verbesserungen

### Phase 2 (Geplant)

- [ ] **Pagination & Filterung** — Unterstützung für `Pageable` für große Datensätze
- [ ] **Soft Deletes** — Logisches Löschen mit Zeitstempel-Tracking
- [ ] **Email-Benachrichtigungen** — Bestätigungs-Emails bei Bewerbungseingang
- [ ] **Erweiterte Suche** — Elasticsearch-Integration für Volltext-Job-Suche
- [ ] **Datei-Uploads** — Resume/CV-Speicherung in der Cloud (AWS S3, GCS)
- [ ] **API Rate Limiting** — Schutz vor Brute-Force-Angriffen
- [ ] **Refresh Tokens** — Verbesserte Sicherheit mit Token-Rotation

### Phase 3 (Langfristig)

- [ ] **Microservices-Architektur** — Aufteilung in Auth, Jobs, Applications Services
- [ ] **Message Queue** — Async Verarbeitung mit RabbitMQ/Kafka
- [ ] **Multi-Tenancy** — Unterstützung mehrerer Recruiter mit isolierten Daten
- [ ] **Analytics Dashboard** — Metriken zu Bewerbungserfolgsquoten
- [ ] **Mobile App** — Native iOS/Android Clients

---

## Abhängigkeiten Highlights

### Core Framework
- **Spring Boot 3.4.1** — Neuestes Framework mit GraalVM-Unterstützung
- **Spring Security 6** — OAuth2-ready, modernes Sicherheitsmodell
- **Spring Data JPA** — Reduziert Boilerplate mit auto-implementierten Abfragen

### Daten & Migration
- **PostgreSQL 14** — Bewährte, Enterprise-Datenbank
- **Flyway** — Versionskontrolle für Datenbank-Schema
- **Lombok** — Reduziert Getter/Setter Boilerplate

### API-Dokumentation
- **SpringDoc OpenAPI 3** — Auto-generiert Swagger/OpenAPI Docs
- **Swagger UI** — Interaktive API-Tests im Browser

### Testing
- **JUnit 5** — Modernes Test-Framework mit Parametrisierung
- **Mockito** — Mock-Objekte für Unit Tests
- **Testcontainers** — Echte Datenbank für Integration Tests

### Mapping
- **MapStruct 1.5.5** — Compile-Time, typsicheres DTO-Mapping
- **JJWT 0.12.x** — JWT Generierung & Validierung

---

## Metriken & Überwachung

### Anwendungs-Gesundheit

Zugang über: **http://localhost:8080/actuator/health**

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL"
      }
    }
  }
}
```

### Leistungs-Überwachung

- Aktivieren mit: `management.endpoints.web.exposure.include=*`
- Metriken anschauen: `/actuator/metrics`
- Häufige Endpunkte:
    - `/actuator/metrics/jvm.memory.usage`
    - `/actuator/metrics/http.server.requests`
    - `/actuator/metrics/process.uptime`

---

## Verifikations-Checkliste

Dieses README wurde gegen die tatsächliche Projektstruktur überprüft:

- ✅ **Package-Struktur** — Entspricht `com.nikolaspc.jobapp` mit allen 8 Ebenen
- ✅ **Controller** — Alle 4 Controller dokumentiert
- ✅ **Services** — Interface + Implementierungs-Muster verifiziert
- ✅ **Repositories** — Benutzerdefinierte Abfrage-Methoden aufgelistet
- ✅ **Sicherheit** — JWT HS512, BCrypt(12), RBAC implementiert
- ✅ **Testing** — 9 Test-Klassen mit Unit & Integration Tests
- ✅ **Datenbank** — Schema, Migrationen, Relationships dokumentiert
- ✅ **Konfiguration** — Alle Config-Klassen & Umgebungsvariablen aufgelistet
- ✅ **API-Endpunkte** — 12+ Endpunkte mit rollenbasiertem Zugriff
- ✅ **DTOs** — Record-basierte & POJO Muster verwendet
- ✅ **Exception-Handling** — GlobalExceptionHandler mit custom Exceptions

---

## Mitwirkende

Beiträge sind willkommen. Bitte beachten Sie:

1. Code folgt den Projekt-Style-Richtlinien
2. Alle Tests bestehen: `./mvnw clean verify`
3. Neue Features enthalten Tests
4. Commit-Nachrichten sind beschreibend

---

## Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert. Siehe [LICENSE](LICENSE) Datei für Details.

---

## Autor

**Nikolas Pérez Cvjetkovic**  
Software Developer | Based in Germany 🇩🇪

📧 [n.perez.cvjetkovic@gmail.com](mailto:n.perez.cvjetkovic@gmail.com)  
💼 [LinkedIn](https://linkedin.com) | 🐙 [GitHub](https://github.com/Nikolaspc)

---

## Unterstützung

Bei Problemen, Feature-Anfragen oder Fragen:

- 🐛 [Issue öffnen](https://github.com/Nikolaspc/job-application-management-backend/issues)
- 💬 [Diskussion starten](https://github.com/Nikolaspc/job-application-management-backend/discussions)

---

**Zuletzt aktualisiert:** Januar 2026  
**Status:** ✅ Production-Ready | Vollständig dokumentiert | Enterprise Grade