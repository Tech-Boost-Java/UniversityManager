package com.softserve.academy.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Test configuration class to disable certain auto-configurations during tests.
 */
@Configuration
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class})
@Profile("test")
public class TestConfig {
    // This class is intentionally empty. Its purpose is to provide configuration
    // for test environment through annotations.
}