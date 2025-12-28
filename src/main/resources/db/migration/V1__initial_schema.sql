-- ============================================
-- V1__initial_schema.sql
-- Initial database schema for Job Application Management System
-- ============================================

-- Job Offers Table (PLURAL: job_offers)
CREATE TABLE job_offers (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    employment_type VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for job_offers
CREATE INDEX idx_job_offers_active ON job_offers(active);
CREATE INDEX idx_job_offers_location ON job_offers(location);
CREATE INDEX idx_job_offers_created_at ON job_offers(created_at);

-- Candidates Table (PLURAL: candidates)
CREATE TABLE candidates (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for candidates
CREATE INDEX idx_candidates_email ON candidates(email);
CREATE INDEX idx_candidates_last_name ON candidates(last_name);

-- Job Applications Table (PLURAL: job_applications)
CREATE TABLE job_applications (
    id BIGSERIAL PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    job_offer_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_application_candidate
        FOREIGN KEY (candidate_id)
        REFERENCES candidates (id)
        ON DELETE CASCADE,
        
    CONSTRAINT fk_application_job_offer
        FOREIGN KEY (job_offer_id)
        REFERENCES job_offers (id)
        ON DELETE CASCADE,
        
    CONSTRAINT uq_candidate_job_offer
        UNIQUE (candidate_id, job_offer_id)
);

-- Indexes for job_applications
CREATE INDEX idx_applications_candidate ON job_applications(candidate_id);
CREATE INDEX idx_applications_job_offer ON job_applications(job_offer_id);
CREATE INDEX idx_applications_status ON job_applications(status);

-- Insert sample data for testing
INSERT INTO job_offers (title, description, location, employment_type, active) VALUES
('Backend Java Developer', 'We are looking for an experienced Java developer with Spring Boot expertise. You will work on building scalable microservices and RESTful APIs.', 'Berlin, Germany', 'FULL_TIME', true),
('Senior Software Engineer', 'Join our engineering team to build cutting-edge solutions. Required: 5+ years experience with Java, Spring, and PostgreSQL.', 'Munich, Germany', 'FULL_TIME', true),
('Junior Java Developer', 'Great opportunity for recent graduates. We offer mentorship and training in modern Java technologies.', 'Hamburg, Germany', 'FULL_TIME', true);

INSERT INTO candidates (first_name, last_name, email, date_of_birth) VALUES
('John', 'Doe', 'john.doe@example.com', '1995-05-15'),
('Jane', 'Smith', 'jane.smith@example.com', '1992-08-20'),
('Max', 'Müller', 'max.mueller@example.com', '1998-03-10');