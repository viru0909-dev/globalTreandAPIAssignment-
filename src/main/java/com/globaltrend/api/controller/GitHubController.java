package com.globaltrend.api.controller;

import com.globaltrend.api.model.ApiResponse;
import com.globaltrend.api.model.GitHubRepository;
import com.globaltrend.api.model.GitHubUser;
import com.globaltrend.api.service.GitHubApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GitHub API Controller
 * Provides REST endpoints for GitHub user and repository data
 */
@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
@Slf4j
public class GitHubController {

    private final GitHubApiService gitHubApiService;

    /**
     * List GitHub users with pagination
     * GET /api/github/users?since=0&perPage=10
     */
    @GetMapping("/users")
    public ApiResponse<List<GitHubUser>> getUsers(
            @RequestParam(defaultValue = "0") int since,
            @RequestParam(defaultValue = "10") int perPage) {

        log.info("GET /api/github/users - since: {}, perPage: {}", since, perPage);

        List<GitHubUser> users = gitHubApiService.getUsers(since, perPage);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("count", users.size());
        metadata.put("since", since);
        metadata.put("perPage", perPage);

        return ApiResponse.success(users, metadata);
    }

    /**
     * Get a single GitHub user by username
     * GET /api/github/users/{username}
     */
    @GetMapping("/users/{username}")
    public ApiResponse<GitHubUser> getUserByUsername(@PathVariable String username) {
        log.info("GET /api/github/users/{}", username);

        GitHubUser user = gitHubApiService.getUserByUsername(username);

        return ApiResponse.success(user);
    }

    /**
     * Get repositories for a GitHub user
     * GET /api/github/users/{username}/repos?page=1&perPage=10
     */
    @GetMapping("/users/{username}/repos")
    public ApiResponse<List<GitHubRepository>> getUserRepositories(
            @PathVariable String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage) {

        log.info("GET /api/github/users/{}/repos - page: {}, perPage: {}", username, page, perPage);

        List<GitHubRepository> repos = gitHubApiService.getUserRepositories(username, page, perPage);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("username", username);
        metadata.put("count", repos.size());
        metadata.put("page", page);
        metadata.put("perPage", perPage);

        return ApiResponse.success(repos, metadata);
    }
}
