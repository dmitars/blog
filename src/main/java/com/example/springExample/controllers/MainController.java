package com.example.springExample.controllers;

import com.example.springExample.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MainController {
    private final PostService postService;

    @Autowired
    public MainController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model) {
        var list = postService.findLastPosts(15);
        model.addAttribute("posts",list);
        model.addAttribute("title", "Главная страница");
        return "home";
    }


    @RequestMapping(value = "/access-denied", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public String actionAccess(Model model){
        return "access-denied";
    }
}
