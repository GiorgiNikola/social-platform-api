package com.socialplatformapi.dto.post;

import com.socialplatformapi.dto.comment.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class PostResponse {
    private Long postId;
    private String text;
    private LocalDateTime postDate;
    private String authorUsername;
    private List<CommentResponse> comments;
    private List<String> likedByUsernames;
}
