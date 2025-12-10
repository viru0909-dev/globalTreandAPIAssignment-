package com.globaltrend.api.model;

import lombok.Data;

/**
 * Post Model for JSONPlaceholder API
 * Represents a blog post
 */
@Data
public class Post {

    private Long id;

    private Long userId;

    private String title;

    private String body;
}
