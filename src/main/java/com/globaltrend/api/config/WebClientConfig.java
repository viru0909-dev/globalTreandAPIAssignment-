package com.globaltrend.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * WebClient Configuration for API Integration
 * 
 * Configures WebClient beans for GitHub and JSONPlaceholder APIs
 * with appropriate timeout settings and headers.
 */
@Configuration
public class WebClientConfig {

    @Value("${api.github.base-url}")
    private String githubBaseUrl;

    @Value("${api.github.timeout}")
    private int githubTimeout;

    @Value("${api.jsonplaceholder.base-url}")
    private String jsonPlaceholderBaseUrl;

    @Value("${api.jsonplaceholder.timeout}")
    private int jsonPlaceholderTimeout;

    /**
     * WebClient for GitHub API
     */
    @Bean(name = "githubWebClient")
    public WebClient githubWebClient() {
        return WebClient.builder()
                .baseUrl(githubBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Global-Trend-API-Integration")
                .build();
    }

    /**
     * WebClient for JSONPlaceholder API
     */
    @Bean(name = "jsonPlaceholderWebClient")
    public WebClient jsonPlaceholderWebClient() {
        return WebClient.builder()
                .baseUrl(jsonPlaceholderBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
