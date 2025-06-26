package com.socialplatformapi.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String text;
    private String commenterUsername;
    private Long postId;
    private LocalDateTime commentDate;
}
