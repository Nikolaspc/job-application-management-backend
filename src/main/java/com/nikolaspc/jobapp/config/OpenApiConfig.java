package com.nikolaspc.jobapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI / Swagger documentation.
 * Access UI at: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Application Management API")
                        .version("1.0")
                        .description("Professional Backend REST API for managing recruitment processes. " +
                                "Features: JWT Security, Role-Based Access, Flyway Migrations.")
                        .contact(new Contact()
                                .name("Nikolas Pérez Cvjetkovic")
                                .email("n.perez.cvjetkovic@gmail.com")
                                .url("https://github.com/Nikolaspc"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server (Local)")
                ));
    }
}