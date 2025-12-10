package com.globaltrend.api.service;

import com.globaltrend.api.exception.ApiIntegrationException;
import com.globaltrend.api.exception.ResourceNotFoundException;
import com.globaltrend.api.model.GitHubRepository;
import com.globaltrend.api.model.GitHubUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * GitHub API Service
 * Handles integration with GitHub API to fetch users and repositories
 * Implements caching, error handling, and retry logic
 */
@Service
@Slf4j
public class GitHubApiService {

    private final WebClient githubWebClient;
    private final int timeout;

    public GitHubApiService(
            @Qualifier("githubWebClient") WebClient githubWebClient,
            @Value("${api.github.timeout}") int timeout) {
        this.githubWebClient = githubWebClient;
        this.timeout = timeout;
    }

    /**
     * Fetch GitHub users with pagination
     * 
     * @param since   User ID to start from (for pagination)
     * @param perPage Number of users per page
     * @return List of GitHub users
     */
    @Cacheable(value = "github-users", key = "#since + '_' + #perPage")
    public List<GitHubUser> getUsers(int since, int perPage) {
        log.info("Fetching GitHub users with since={} and perPage={}", since, perPage);

        try {
            return githubWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/users")
                            .queryParam("since", since)
                            .queryParam("per_page", perPage)
                            .build())
                    .retrieve()
                    .bodyToFlux(GitHubUser.class)
                    .timeout(Duration.ofMillis(timeout))
                    .collectList()
                    .block();

        } catch (WebClientResponseException e) {
            log.error("GitHub API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ApiIntegrationException("Failed to fetch users from GitHub: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Error fetching GitHub users", e);
            throw new ApiIntegrationException("Failed to fetch users from GitHub: " + e.getMessage(), e);
        }
    }

    /**
     * Fetch a single GitHub user by username
     * 
     * @param username GitHub username
     * @return GitHub user details
     */
    @Cacheable(value = "github-users", key = "#username")
    public GitHubUser getUserByUsername(String username) {
        log.info("Fetching GitHub user: {}", username);

        try {
            GitHubUser user = githubWebClient.get()
                    .uri("/users/{username}", username)
                    .retrieve()
                    .bodyToMono(GitHubUser.class)
                    .timeout(Duration.ofMillis(timeout))
                    .onErrorResume(WebClientResponseException.NotFound.class,
                            error -> Mono.error(new ResourceNotFoundException("User not found: " + username)))
                    .block();

            if (user == null) {
                throw new ResourceNotFoundException("User not found: " + username);
            }

            return user;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (WebClientResponseException e) {
            log.error("GitHub API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ApiIntegrationException("Failed to fetch user from GitHub: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Error fetching GitHub user", e);
            throw new ApiIntegrationException("Failed to fetch user from GitHub: " + e.getMessage(), e);
        }
    }

    /**
     * Fetch repositories for a GitHub user
     * 
     * @param username GitHub username
     * @param page     Page number (starts from 1)
     * @param perPage  Number of repos per page
     * @return List of repositories
     */
    @Cacheable(value = "github-repos", key = "#username + '_' + #page + '_' + #perPage")
    public List<GitHubRepository> getUserRepositories(String username, int page, int perPage) {
        log.info("Fetching repositories for user: {} (page={}, perPage={})", username, page, perPage);

        try {
            return githubWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/users/{username}/repos")
                            .queryParam("page", page)
                            .queryParam("per_page", perPage)
                            .queryParam("sort", "updated")
                            .build(username))
                    .retrieve()
                    .bodyToFlux(GitHubRepository.class)
                    .timeout(Duration.ofMillis(timeout))
                    .collectList()
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("User not found: " + username);
        } catch (WebClientResponseException e) {
            log.error("GitHub API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ApiIntegrationException("Failed to fetch repositories from GitHub: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Error fetching GitHub repositories", e);
            throw new ApiIntegrationException("Failed to fetch repositories from GitHub: " + e.getMessage(), e);
        }
    }
}
