package com.demo.domain.repository.support;

import com.demo.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> findAll();
    Optional<Post> findById(Long id);
    List<Post> findByAuthorId(Long authorId);
    Post save(Post post);
    void update(Long postId, Post updatedPost);
    void delete(Post post);
}
