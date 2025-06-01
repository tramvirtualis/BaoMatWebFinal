package com.WebDoChoi.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashingUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hash(String s) {
        return encoder.encode(s);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
