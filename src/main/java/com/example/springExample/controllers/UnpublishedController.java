package com.example.springExample.controllers;

import com.example.springExample.models.Post;
import com.example.springExample.repo.PostRepository;
import com.example.springExample.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UnpublishedController {
    private final PostService postService;

    @Autowired
    public UnpublishedController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/unpublished")
    public String unpublished(Model model) {
        Iterable<Post> posts = postService.findAllUnchecked();
        model.addAttribute("posts",posts);
        model.addAttribute("title","Блог. Неопубликованное");
        return "unpublished";
    }

    @PostMapping("/unpublished/{id}/confirm")
    public String unpublishedConfirm(@PathVariable(value = "id") Post post) {
        postService.updatePost(post);
        return "redirect:/unpublished";
    }
}
