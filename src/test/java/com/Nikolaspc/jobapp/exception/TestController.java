package com.Nikolaspc.jobapp.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/not-found")
    public void throwNotFound() {
        throw new ResourceNotFoundException("Resource not found");
    }

    @GetMapping("/test/generic-error")
    public void throwGeneric() {
        throw new RuntimeException("Generic error");
    }
}