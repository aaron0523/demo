package com.demo.web.controller;

import com.demo.domain.post.Post;
import com.demo.domain.service.post.PostService;
import com.demo.web.dto.post.PostCreateDto;
import com.demo.web.dto.post.PostUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/index")
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/detail/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "detailPostForm";
        } else {
            // Handle post not found error
            return "error";
        }
    }

    @GetMapping("/add")
    public String showAddPostForm() {
        return "addPostForm";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute PostCreateDto createDto, @RequestParam("authorId") Long authorId) {
        postService.createPost(createDto, authorId);
        return "redirect:/posts/index";
    }

    @GetMapping("/edit/{id}")
    public String showEditPostForm(@PathVariable("id") Long id, Model model) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "editPostForm";
        } else {
            // Handle post not found error
            return "error";
        }
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable("id") Long id, @ModelAttribute PostUpdateDto updateDto) {
        postService.updatePost(id, updateDto);
        return "redirect:/posts/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/posts/index";
    }
}
