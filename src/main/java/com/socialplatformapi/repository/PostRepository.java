package com.socialplatformapi.repository;

import com.socialplatformapi.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllBy(Pageable pageable);
    List<Post> findAllByPosterUsername(String username, Pageable pageable);
}
