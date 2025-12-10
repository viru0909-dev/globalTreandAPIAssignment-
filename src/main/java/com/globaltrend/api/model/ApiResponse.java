package com.globaltrend.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API Response Wrapper
 * Provides consistent response structure for all API endpoints
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private Object metadata;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Request successful", data, null);
    }

    public static <T> ApiResponse<T> success(T data, Object metadata) {
        return new ApiResponse<>(true, "Request successful", data, metadata);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, null);
    }
}
