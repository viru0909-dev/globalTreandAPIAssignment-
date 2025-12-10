package com.globaltrend.api.controller;

import com.globaltrend.api.model.ApiResponse;
import com.globaltrend.api.model.Post;
import com.globaltrend.api.model.User;
import com.globaltrend.api.service.JsonPlaceholderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Posts and Users Controller
 * Provides REST endpoints for JSONPlaceholder posts and users data
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final JsonPlaceholderService jsonPlaceholderService;

    /**
     * List all posts with optional filtering
     * GET /api/posts?userId=1&limit=10
     */
    @GetMapping("/posts")
    public ApiResponse<List<Post>> getPosts(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer limit) {

        log.info("GET /api/posts - userId: {}, limit: {}", userId, limit);

        List<Post> posts = jsonPlaceholderService.getPosts(userId, limit);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("count", posts.size());
        if (userId != null) {
            metadata.put("filteredByUserId", userId);
        }
        if (limit != null) {
            metadata.put("limit", limit);
        }

        return ApiResponse.success(posts, metadata);
    }

    /**
     * Get a single post by ID
     * GET /api/posts/{id}
     */
    @GetMapping("/posts/{id}")
    public ApiResponse<Post> getPostById(@PathVariable Long id) {
        log.info("GET /api/posts/{}", id);

        Post post = jsonPlaceholderService.getPostById(id);

        return ApiResponse.success(post);
    }

    /**
     * List all users
     * GET /api/users
     */
    @GetMapping("/users")
    public ApiResponse<List<User>> getUsers() {
        log.info("GET /api/users");

        List<User> users = jsonPlaceholderService.getUsers();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("count", users.size());

        return ApiResponse.success(users, metadata);
    }

    /**
     * Get a single user by ID
     * GET /api/users/{id}
     */
    @GetMapping("/users/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{}", id);

        User user = jsonPlaceholderService.getUserById(id);

        return ApiResponse.success(user);
    }
}
