package com.softserve.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

/**
 * Test application class for running tests.
 * This class is a simplified version of the main Application class,
 * without the @PostConstruct method that initializes test data.
 */
@SpringBootApplication
@Profile("test")
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}