package com.WebDoChoi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class SecurityLogger {
    private static final Logger logger = LoggerFactory.getLogger("com.WebDoChoi.security");

    public static void logLoginSuccess(String username, HttpServletRequest request) {
        logger.info("Successful login - User: {} | IP: {} | Session: {}", 
            username, 
            getClientIP(request),
            request.getSession().getId());
    }

    public static void logLoginFailure(String username, HttpServletRequest request) {
        logger.warn("Failed login attempt - User: {} | IP: {}", 
            username, 
            getClientIP(request));
    }

    public static void logUnauthorizedAccess(String username, String resource, HttpServletRequest request) {
        logger.warn("Unauthorized access attempt - User: {} | Resource: {} | IP: {}", 
            username, 
            resource,
            getClientIP(request));
    }

    public static void logCsrfViolation(HttpServletRequest request) {
        logger.warn("CSRF violation detected - URI: {} | IP: {}", 
            request.getRequestURI(),
            getClientIP(request));
    }

    public static void logPasswordChange(String username, boolean success, HttpServletRequest request) {
        if (success) {
            logger.info("Password changed successfully - User: {} | IP: {}", 
                username,
                getClientIP(request));
        } else {
            logger.warn("Failed password change attempt - User: {} | IP: {}", 
                username,
                getClientIP(request));
        }
    }

    public static void logUserCreation(String username, String role, HttpServletRequest request) {
        logger.info("New user created - Username: {} | Role: {} | IP: {}", 
            username,
            role,
            getClientIP(request));
    }

    public static void logUserModification(String username, String modifiedBy, HttpServletRequest request) {
        logger.info("User modified - Username: {} | Modified by: {} | IP: {}", 
            username,
            modifiedBy,
            getClientIP(request));
    }

    private static String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
} 