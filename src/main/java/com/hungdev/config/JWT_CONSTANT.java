package com.hungdev.config;

public class JWT_CONSTANT {
    public static final String SECRET_KEY = System.getenv("JWT_SECRET");
    public static final String JWT_HEADER = "Authorization";
}
