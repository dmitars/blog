package com.example.springExample.controllers;

import com.example.springExample.models.Post;
import com.example.springExample.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class UnpublishedController {
    @Autowired(required = true)
    private PostRepository postRepository;

    @GetMapping("/unpublished")
    public String unpublished(Model model) {
        Iterable<Post> posts = postRepository.findAllUnchecked();
        model.addAttribute("posts",posts);
        model.addAttribute("title","Блог. Неопубликованное");
        return "unpublished";
    }

    /*@GetMapping("/unpublished/{id}")
    public String unpublishedDetails(@PathVariable(value = "id") long postId, Model model) {
        if(!postRepository.existsById(postId))
            return "redirect:/blog";
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post",res);
        return "blog-details";
    }*/

    @PostMapping("/unpublished/{id}/confirm")
    public String unpublishedConfirm(@PathVariable(value = "id") long postId, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        if(post.getOriginalId()!=null)
            postRepository.deleteById(post.getOriginalId());
        post.setChecked(true);
        postRepository.save(post);
        return "redirect:/unpublished";
    }
}
