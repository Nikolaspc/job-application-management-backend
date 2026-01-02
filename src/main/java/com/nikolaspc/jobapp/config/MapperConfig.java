package com.nikolaspc.jobapp.config;

import org.mapstruct.ReportingPolicy;

@org.mapstruct.MapperConfig(
        componentModel = "spring", // Injectable via @Autowired
        unmappedTargetPolicy = ReportingPolicy.IGNORE // Don't fail if DTO has fewer fields than Entity
)
public interface MapperConfig {
}