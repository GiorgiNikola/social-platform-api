package com.socialplatformapi.dto;

import com.socialplatformapi.dto.comment.CommentResponse;
import com.socialplatformapi.dto.post.PostResponse;
import com.socialplatformapi.model.Comment;
import com.socialplatformapi.model.Post;
import com.socialplatformapi.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapper {

    public PostResponse postToDto(Post post) {
        List<CommentResponse> commentResponses = post.getComments() == null ? List.of() :
                post.getComments().stream().map(this::commentToDto).toList();

        List<String> likedBy = post.getLikedByUsers() == null ? List.of() :
                post.getLikedByUsers().stream().map(User::getUsername).toList();

        return new PostResponse(
                post.getId(),
                post.getText(),
                post.getPostDate(),
                post.getPoster().getUsername(),
                commentResponses,
                likedBy
        );
    }

    public CommentResponse commentToDto(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getCommentText(),
                comment.getCommenter().getUsername(),
                comment.getPost().getId(),
                comment.getCommentDate()
        );
    }
}
