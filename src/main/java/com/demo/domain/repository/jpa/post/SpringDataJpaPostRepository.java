package com.demo.domain.repository.jpa.post;

import com.demo.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaPostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long authorId);
}
