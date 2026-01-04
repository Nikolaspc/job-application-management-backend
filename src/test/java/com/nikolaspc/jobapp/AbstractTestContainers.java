package com.nikolaspc.jobapp;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestContainers {

    /**
     * Shared PostgreSQL container instance for integration tests.
     * Uses Alpine version for faster pull times in CI environments.
     */
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14-alpine")
                    .withDatabaseName("job_application_test_db")
                    .withUsername("test_user")
                    .withPassword("test_pass");

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        // Start container if not already running
        if (!postgreSQLContainer.isRunning()) {
            postgreSQLContainer.start();
        }

        // Map container properties to Spring datasource configuration
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }
}