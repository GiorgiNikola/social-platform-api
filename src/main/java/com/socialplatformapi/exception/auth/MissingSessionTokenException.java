package com.socialplatformapi.exception.auth;

public class MissingSessionTokenException extends RuntimeException {
    public MissingSessionTokenException() {
        super("Missing session token");
    }
}
