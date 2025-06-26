package com.socialplatformapi.exception.post;

public class InvalidAuthorException extends RuntimeException {
    public InvalidAuthorException() {
        super("You are not the author of this post");
    }
}
