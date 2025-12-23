package com.nikolaspc.jobapp.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
        int status,
        String error,
        String message,
        OffsetDateTime timestamp,
        String path,
        List<FieldError> errors
) {
    public record FieldError(String field, String message) {}
}
