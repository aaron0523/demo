package com.demo.domain.repository.jpa.post;

import com.demo.domain.post.Post;
import com.demo.domain.post.PostType;
import com.demo.domain.repository.support.PostRepository;
import com.demo.domain.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaPostQuerydslRepository extends QuerydslRepositorySupport implements PostRepository {

    public JpaPostQuerydslRepository() {
        super(Post.class);
    }

    private SpringDataJpaPostRepository jpaPostRepository;

    public List<Post> findAll() {
        return jpaPostRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return jpaPostRepository.findById(id);
    }

    public List<Post> findByAuthorId(Long authorId) {
        return jpaPostRepository.findByAuthorId(authorId);
    }

    public Post save(Post post) {
        return jpaPostRepository.save(post);
    }

    public void update(Long postId, Post updatedPost) {
        Optional<Post> existingPost = jpaPostRepository.findById(postId);
        existingPost.ifPresent(post -> {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            // ... 업데이트할 필드들 추가
            jpaPostRepository.save(post);
        });
    }

    public void delete(Post post) {
        jpaPostRepository.delete(post);
    }

}
