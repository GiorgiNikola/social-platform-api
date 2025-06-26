package com.socialplatformapi.controller;

import com.socialplatformapi.dto.post.PostRequest;
import com.socialplatformapi.dto.post.PostResponse;
import com.socialplatformapi.model.Post;
import com.socialplatformapi.service.AuthorizationService;
import com.socialplatformapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthorizationService authorizationService;

    @Operation(
            summary = "Create post",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true, description = "Session token")
            }
    )
    @PostMapping
    public PostResponse createPost(
            @RequestBody PostRequest request,
            HttpServletRequest httpRequest) {

        var user = authorizationService.getLoggedInUser(httpRequest);

        return postService.createPost(request, user);
    }

    @Operation(
            summary = "Update post",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true, description = "Session token")
            }
    )
    @PutMapping("/{id}")
    public PostResponse updatePost(
            @PathVariable Long id,
            @RequestBody PostRequest request,
            HttpServletRequest httpRequest
    ) {
        var user = authorizationService.getLoggedInUser(httpRequest);

        return postService.updatePost(id, request, user);
    }

    @Operation(
            summary = "Delete post",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true, description = "Session token")
            }
    )
    @DeleteMapping("/{id}")
    public String deletePost(
            @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {
        var user = authorizationService.getLoggedInUser(httpRequest);

        postService.deletePost(id, user);
        return "Post deleted successfully";
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPostResponse(id);
    }

    @GetMapping
    public List<PostResponse> getAllPosts(@RequestParam(defaultValue = "0") int page) {
        return postService.getAllPosts(PageRequest.of(page, 10, Sort.Direction.ASC, "id"));
    }

    @GetMapping("/by-user/{username}")
    public List<PostResponse> getPostsByUser(@PathVariable String username
            ,@RequestParam(defaultValue = "0") int page) {
        return postService.getPostsByUser(username,
                PageRequest.of(page, 10, Sort.Direction.ASC, "id"));
    }

    @Operation(
            summary = "Like post",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true, description = "Session token")
            }
    )
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable Long id, HttpServletRequest request) {
        var user = authorizationService.getLoggedInUser(request);
        postService.likePost(id, user);
        return ResponseEntity.ok("Post liked");
    }

    @Operation(
            summary = "Unlike post",
            parameters = {
                    @Parameter(name = "X-Session-Token", in = ParameterIn.HEADER, required = true, description = "Session token")
            }
    )
    @DeleteMapping("/{id}/like")
    public ResponseEntity<String> unlikePost(@PathVariable Long id, HttpServletRequest request) {
        var user = authorizationService.getLoggedInUser(request);
        postService.unlikePost(id, user);
        return ResponseEntity.ok("Like removed");
    }

}
