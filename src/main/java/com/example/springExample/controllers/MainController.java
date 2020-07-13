package com.example.springExample.controllers;

import com.example.springExample.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class MainController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        var posts = postRepository.findAllChecked();
        var list = StreamSupport.stream(posts.spliterator(),false).collect(Collectors.toList());
        if(list.size()>15)
            list = list.subList(0,15);
        model.addAttribute("posts",list);
        return "home";
    }

    @GetMapping("/access-denied")
    public String blockAccess(Model model){
        return "access-denied";
    }

    @PostMapping("/access-denied")
    public String actionAccess(Model model){
        return "access-denied";
    }
}
