package com.globaltrend.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main Spring Boot Application for Global Trend API Integration Assignment
 * 
 * This application integrates with public REST APIs (GitHub and JSONPlaceholder)
 * to fetch, cache, and display data with filtering capabilities.
 */
@SpringBootApplication
@EnableCaching
public class GlobalTrendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalTrendApiApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Global Trend API Integration Started!");
        System.out.println("Access the application at: http://localhost:8080");
        System.out.println("========================================\n");
    }
}
