package com.socialplatformapi.exception.comment;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("comment with id " + id + " not found");
    }
}
