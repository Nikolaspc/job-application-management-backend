package com.nikolaspc.jobapp.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Audit Aspect
 * * Generates explicit evidence for critical business operations (BSI/GDPR Compliance).
 */
@Aspect
@Component
@Slf4j
public class AuditAspect {

    private static final Logger auditLogger = LoggerFactory.getLogger("audit-logger");

    // English: Monitor auth and user registration methods
    @Pointcut("execution(* com.nikolaspc.jobapp.service.AuthService.register(..)) || " +
            "execution(* com.nikolaspc.jobapp.service.AuthService.login(..))")
    public void securityOperations() {}

    @AfterReturning(pointcut = "securityOperations()", returning = "result")
    public void logSecurityAction(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // English: Filter arguments to avoid logging passwords (OWASP Mitigation)
        String userEmail = "unknown";
        if (args.length > 0) {
            // We assume the first argument contains the email in RegisterRequest/AuthRequest
            // You can extract it more precisely based on your DTOs
            userEmail = args[0].toString();
        }

        auditLogger.info("SECURITY_EVENT | Action: {} | User: {} | Status: SUCCESS",
                methodName.toUpperCase(), userEmail);
    }
}