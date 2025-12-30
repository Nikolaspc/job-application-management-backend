package com.nikolaspc.jobapp.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTestController {

    @GetMapping("/test/not-found")
    public void throwNotFound() {
        throw new ResourceNotFoundException("Candidate", 1L);
    }

    @GetMapping("/test/generic-error")
    public void throwGeneric() {
        throw new RuntimeException("Unexpected");
    }
}