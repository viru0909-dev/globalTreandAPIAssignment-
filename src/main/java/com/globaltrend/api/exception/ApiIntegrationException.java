package com.globaltrend.api.exception;

/**
 * Custom exception for API integration errors
 */
public class ApiIntegrationException extends RuntimeException {

    public ApiIntegrationException(String message) {
        super(message);
    }

    public ApiIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
