package com.globaltrend.api.service;

import com.globaltrend.api.exception.ApiIntegrationException;
import com.globaltrend.api.exception.ResourceNotFoundException;
import com.globaltrend.api.model.Post;
import com.globaltrend.api.model.User;
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
import java.util.stream.Collectors;

/**
 * JSONPlaceholder API Service
 * Handles integration with JSONPlaceholder API to fetch posts and users
 * Implements caching and error handling
 */
@Service
@Slf4j
public class JsonPlaceholderService {

    private final WebClient jsonPlaceholderWebClient;
    private final int timeout;

    public JsonPlaceholderService(
            @Qualifier("jsonPlaceholderWebClient") WebClient jsonPlaceholderWebClient,
            @Value("${api.jsonplaceholder.timeout}") int timeout) {
        this.jsonPlaceholderWebClient = jsonPlaceholderWebClient;
        this.timeout = timeout;
    }

    /**
     * Fetch all posts with optional filtering
     * 
     * @param userId Optional user ID filter
     * @param limit  Optional limit on number of results
     * @return List of posts
     */
    @Cacheable(value = "posts", key = "#userId != null ? #userId : 'all'")
    public List<Post> getPosts(Long userId, Integer limit) {
        log.info("Fetching posts (userId={}, limit={})", userId, limit);

        try {
            List<Post> posts = jsonPlaceholderWebClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder.path("/posts");
                        if (userId != null) {
                            builder.queryParam("userId", userId);
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .bodyToFlux(Post.class)
                    .timeout(Duration.ofMillis(timeout))
                    .collectList()
                    .block();

            // Apply limit if specified
            if (posts != null && limit != null && limit > 0) {
                posts = posts.stream()
                        .limit(limit)
                        .collect(Collectors.toList());
            }

            return posts;

        } catch (WebClientResponseException e) {
            log.error("JSONPlaceholder API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ApiIntegrationException("Failed to fetch posts: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Error fetching posts", e);
            throw new ApiIntegrationException("Failed to fetch posts: " + e.getMessage(), e);
        }
    }

    /**
     * Fetch a single post by ID
     * 
     * @param id Post ID
     * @return Post details
     */
    @Cacheable(value = "posts", key = "'post_' + #id")
    public Post getPostById(Long id) {
        log.info("Fetching post with ID: {}", id);

        try {
            Post post = jsonPlaceholderWebClient.get()
                    .uri("/posts/{id}", id)
                    .retrieve()
                    .bodyToMono(Post.class)
                    .timeout(Duration.ofMillis(timeout))
                    .onErrorResume(WebClientResponseException.NotFound.class,
                            error -> Mono.error(new ResourceNotFoundException("Post not found with ID: " + id)))
                    .block();

            if (post == null) {
                throw new ResourceNotFoundException("Post not found with ID: " + id);
            }

            return post;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (WebClientResponseException e) {
            log.error("JSONPlaceholder API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ApiIntegrationException("Failed to fetch post: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Error fetching post", e);
            throw new ApiIntegrationException("Failed to fetch post: " + e.getMessage(), e);
        }
    }

    /**
     * Fetch all users
     * 
     * @return List of users
     */
    @Cacheable("users")
    public List<User> getUsers() {
        log.info("Fetching all users");

        try {
            return jsonPlaceholderWebClient.get()
                    .uri("/users")
                    .retrieve()
                    .bodyToFlux(User.class)
                    .timeout(Duration.ofMillis(timeout))
                    .collectList()
                    .block();

        } catch (WebClientResponseException e) {
            log.error("JSONPlaceholder API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ApiIntegrationException("Failed to fetch users: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Error fetching users", e);
            throw new ApiIntegrationException("Failed to fetch users: " + e.getMessage(), e);
        }
    }

    /**
     * Fetch a single user by ID
     * 
     * @param id User ID
     * @return User details
     */
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);

        try {
            User user = jsonPlaceholderWebClient.get()
                    .uri("/users/{id}", id)
                    .retrieve()
                    .bodyToMono(User.class)
                    .timeout(Duration.ofMillis(timeout))
                    .onErrorResume(WebClientResponseException.NotFound.class,
                            error -> Mono.error(new ResourceNotFoundException("User not found with ID: " + id)))
                    .block();

            if (user == null) {
                throw new ResourceNotFoundException("User not found with ID: " + id);
            }

            return user;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (WebClientResponseException e) {
            log.error("JSONPlaceholder API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ApiIntegrationException("Failed to fetch user: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error("Error fetching user", e);
            throw new ApiIntegrationException("Failed to fetch user: " + e.getMessage(), e);
        }
    }
}
