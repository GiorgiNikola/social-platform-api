package com.socialplatformapi.service;

import com.socialplatformapi.dto.Mapper;
import com.socialplatformapi.dto.comment.CommentRequest;
import com.socialplatformapi.dto.comment.CommentResponse;
import com.socialplatformapi.dto.comment.CommentUpdateRequest;
import com.socialplatformapi.exception.comment.CommentNotFoundException;
import com.socialplatformapi.exception.post.InvalidAuthorException;
import com.socialplatformapi.exception.post.PostNotExistsException;
import com.socialplatformapi.model.Comment;
import com.socialplatformapi.model.Post;
import com.socialplatformapi.model.User;
import com.socialplatformapi.repository.CommentRepository;
import com.socialplatformapi.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final Mapper mapper;

    public CommentResponse addComment(CommentRequest request, User user) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostNotExistsException(request.getPostId()));

        Comment comment = new Comment();
        comment.setCommentText(request.getText());
        comment.setCommentDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setCommenter(user);

        Comment saved = commentRepository.save(comment);

        return new CommentResponse(
                saved.getId(),
                saved.getCommentText(),
                user.getUsername(),
                post.getId(),
                saved.getCommentDate()
        );
    }

    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request, User user) {
        Comment comment = getComment(commentId, user);

        comment.setCommentText(request.getText());
        comment.setCommentDate(LocalDateTime.now());
        commentRepository.save(comment);

        return mapper.commentToDto(comment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        boolean isAuthorOfComment = comment.getCommenter().getId().equals(user.getId());
        boolean isPostOwner = comment.getPost().getPoster().getId().equals(user.getId());

        if (!isAuthorOfComment && !isPostOwner) {
            throw new InvalidAuthorException();
        }

        comment.getPost().getComments().remove(comment);

        commentRepository.delete(comment);
    }

    public Comment getComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getCommenter().getId().equals(user.getId())) {
            throw new InvalidAuthorException();
        }
        return comment;
    }

    public List<CommentResponse> getCommentsByPost(Long postId, Pageable pageable) {
        return commentRepository.findAllByPostId(postId, pageable)
                .stream()
                .map(mapper::commentToDto)
                .toList();
    }

    public List<CommentResponse> getCommentsByUser(String username, Pageable pageable) {
        return commentRepository.findAllByCommenterUsername(username, pageable)
                .stream()
                .map(mapper::commentToDto)
                .toList();
    }

}
