package com.WebDoChoi.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LoginAttemptManager {
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION_MINUTES = 15;

    private static final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    private static class LoginAttempt {
        private int attempts;
        private long lockoutTime;

        public LoginAttempt() {
            this.attempts = 0;
            this.lockoutTime = 0;
        }
    }

    public static boolean isLocked(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) {
            return false;
        }

        if (attempt.lockoutTime > 0) {
            if (System.currentTimeMillis() < attempt.lockoutTime) {
                return true;
            } else {
                // Reset if lockout period has expired
                attempt.attempts = 0;
                attempt.lockoutTime = 0;
                return false;
            }
        }
        return false;
    }

    public static void recordFailedAttempt(String username) {
        LoginAttempt attempt = loginAttempts.computeIfAbsent(username, k -> new LoginAttempt());
        attempt.attempts++;

        if (attempt.attempts >= MAX_ATTEMPTS) {
            attempt.lockoutTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(LOCKOUT_DURATION_MINUTES);
        }
    }

    public static void resetAttempts(String username) {
        loginAttempts.remove(username);
    }

    public static long getRemainingLockoutTime(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null || attempt.lockoutTime == 0) {
            return 0;
        }

        long remaining = attempt.lockoutTime - System.currentTimeMillis();
        return remaining > 0 ? remaining : 0;
    }

    public static int getRemainingAttempts(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) {
            return MAX_ATTEMPTS;
        }
        return Math.max(0, MAX_ATTEMPTS - attempt.attempts);
    }
}