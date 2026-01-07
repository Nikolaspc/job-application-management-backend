package com.nikolaspc.jobapp.domain;

/**
 * Enumeration of system roles for RBAC (Role-Based Access Control).
 */
public enum UserRole {
    // English: System administrator with full management capabilities
    ADMIN,
    // English: Company representative who posts jobs and manages applications
    RECRUITER,
    // English: Job seeker who views offers and applies
    CANDIDATE
}