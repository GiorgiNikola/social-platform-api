package com.socialplatformapi.exception.post;

public class PostNotExistsException extends RuntimeException {
    public PostNotExistsException(Long id) {
        super("Post with id " + id + " does not exist");
    }

}
