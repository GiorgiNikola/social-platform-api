package com.socialplatformapi.exception.auth;

public class InvalidSessionTokenException extends RuntimeException {
    public InvalidSessionTokenException() {
        super("Invalid or expired session token");
    }
}
