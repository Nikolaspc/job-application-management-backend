package com.nikolaspc.jobapp.exception;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// English comments as requested:
// This controller is used only for testing GlobalExceptionHandler
@RestController
@Profile("test")
public class ExceptionTestController {

    @GetMapping("/test/not-found")
    public void throwNotFound() {
        // Verification: Ensure your ResourceNotFoundException constructor
        // accepts (String resourceName, Long id)
        throw new ResourceNotFoundException("Candidate", 1L);
    }

    @GetMapping("/test/generic-error")
    public void throwGeneric() {
        throw new RuntimeException("Unexpected");
    }
}