-- English: V1 Initial Schema - Optimized for Data Integrity & Performance

-- 1. Table: users
-- Stores core authentication and identity data
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT uk_users_email UNIQUE (email)
);

-- 2. Table: candidates (Profile extension)
-- One-to-one relationship with users table for candidate-specific data
CREATE TABLE candidates (
    id BIGINT PRIMARY KEY,
    date_of_birth DATE NOT NULL,
    CONSTRAINT fk_candidates_users FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
);

-- 3. Table: job_offers
-- Stores information about available job positions
CREATE TABLE job_offers (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    employment_type VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

-- 4. Table: job_applications
-- Links candidates with job offers; includes a unique constraint to prevent duplicate applications
CREATE TABLE job_applications (
    id BIGSERIAL PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    job_offer_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    applied_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_applications_candidate FOREIGN KEY (candidate_id) REFERENCES candidates (id) ON DELETE CASCADE,
    CONSTRAINT fk_applications_job_offer FOREIGN KEY (job_offer_id) REFERENCES job_offers (id) ON DELETE CASCADE,
    CONSTRAINT uk_candidate_job_offer UNIQUE (candidate_id, job_offer_id)
);

-- English: Indexes for high-performance lookups
-- Note: Email index is redundant if UNIQUE constraint exists in some DBs,
-- but explicitly defining it ensures clarity and performance across all PostgreSQL versions.
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_job_offers_active ON job_offers(active);
CREATE INDEX idx_job_applications_candidate ON job_applications(candidate_id);
CREATE INDEX idx_job_applications_job_offer ON job_applications(job_offer_id);