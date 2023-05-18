package com.demo.domain.service.post;

import com.demo.domain.member.Member;
import com.demo.domain.post.Post;
import com.demo.domain.repository.support.MemberRepository;
import com.demo.domain.repository.support.PostRepository;
import com.demo.web.dto.post.PostCreateDto;
import com.demo.web.dto.post.PostUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostServiceImp implements PostService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostServiceImp(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getPostsByAuthorId(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public Post createPost(PostCreateDto createDto, Long authorId) {
        Optional<Member> author = memberRepository.findById(authorId);
        if (author.isPresent()) {
            Post post = new Post();
            post.setAuthor(author.get());
            // Set other fields from createDto
            // ...
            return postRepository.save(post);
        } else {
            throw new IllegalArgumentException("Invalid author ID: " + authorId);
        }
    }

    public void updatePost(Long postId, PostUpdateDto updateDto) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isPresent()) {
            Post post = existingPost.get();
            // Update post fields from updateDto
            // ...
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("Invalid post ID: " + postId);
        }
    }

    public void deletePost(Long postId) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isPresent()) {
            Post post = existingPost.get();
            postRepository.delete(post);
        } else {
            throw new IllegalArgumentException("Invalid post ID: " + postId);
        }
    }
}