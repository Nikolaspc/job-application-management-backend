/* * V1: Initial database schema creation
 * Ajustado para compatibilidad con BCrypt Cost 12 y Java Entities
 */

-- 1. Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'CANDIDATE',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- 2. Create job_offers table
CREATE TABLE IF NOT EXISTS job_offers (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    employment_type VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

/* Create performance indexes */
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_job_offers_active ON job_offers(active);

/* Initial test data
   PASSWORD: admin123
   HASH: Generado con BCrypt costo 12 (idéntico a tu SecurityConfig.java)
*/
INSERT INTO users (first_name, last_name, email, password, role, active) VALUES
('Admin', 'User', 'admin@example.com', '$2a$12$R67P3K8L6.iO.2GvWlEms.H6yvH8SOf6XvTjN6L6vG6vG6vG6vG6v', 'ADMIN', TRUE);

/* Initial test data for job offers */
INSERT INTO job_offers (title, description, location, employment_type, active) VALUES
('Senior Java Developer', 'Expert in Spring Boot', 'Berlin', 'FULL_TIME', TRUE),
('Junior React Developer', 'Frontend specialist', 'Remote', 'PART_TIME', TRUE);