package com.example.springExample.controllers;

import com.example.springExample.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(value = "/access-denied", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public String actionAccess(Model model){
        return "access-denied";
    }
}
