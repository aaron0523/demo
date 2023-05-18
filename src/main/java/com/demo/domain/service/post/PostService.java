package com.demo.domain.service.post;

import com.demo.domain.post.Post;
import com.demo.web.dto.post.PostCreateDto;
import com.demo.web.dto.post.PostUpdateDto;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<Post> getAllPosts();
    Optional<Post> getPostById(Long postId);
    List<Post> getPostsByAuthorId(Long authorId);
    Post createPost(PostCreateDto createDto, Long authorId);
    void updatePost(Long postId, PostUpdateDto updateDto);
    void deletePost(Long postId);
}
