package com.socialplatformapi.service;

import com.socialplatformapi.dto.Mapper;
import com.socialplatformapi.dto.post.PostRequest;
import com.socialplatformapi.dto.post.PostResponse;
import com.socialplatformapi.exception.post.InvalidAuthorException;
import com.socialplatformapi.exception.post.PostNotExistsException;
import com.socialplatformapi.model.Post;
import com.socialplatformapi.model.User;
import com.socialplatformapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final Mapper mapper;

    public PostResponse createPost(PostRequest request, User user) {
        Post post = new Post();
        post.setText(request.getText());
        post.setPostDate(LocalDateTime.now());
        post.setPoster(user);
        Post savedPost = postRepository.save(post);

        return mapper.postToDto(savedPost);
    }

    public PostResponse updatePost(Long postId,PostRequest request, User user) {
        Post post = getPost(postId, user);

        post.setText(request.getText());
        post.setPostDate(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        return mapper.postToDto(savedPost);
    }

    public void deletePost(Long postId, User user) {
        Post post = getPost(postId, user);
        postRepository.delete(post);
    }

    public Post getPost(Long postId, User user) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new PostNotExistsException(postId);
        }
        Post post = postOptional.get();
        if (!post.getPoster().getId().equals(user.getId())) {
            throw new InvalidAuthorException();
        }
        return post;
    }

    public PostResponse getPostResponse(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new PostNotExistsException(postId);
        }
        Post post = postOptional.get();
        return mapper.postToDto(post);
    }

    public List<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository
                .findAllBy(pageable)
                .stream()
                .map(mapper::postToDto)
                .toList();
    }

    public List<PostResponse> getPostsByUser(String username, Pageable pageable) {
        return postRepository
                .findAllByPosterUsername(username, pageable)
                .stream()
                .map(mapper::postToDto)
                .toList();
    }

    public void likePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistsException(postId));

        post.getLikedByUsers().add(user); // Set არ დაუშვებს დუბლიკატს
        postRepository.save(post);
    }

    public void unlikePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistsException(postId));

        post.getLikedByUsers().remove(user);
        postRepository.save(post);
    }
}
