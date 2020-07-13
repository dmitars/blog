package com.example.springExample.controllers;

import com.example.springExample.models.Role;
import com.example.springExample.models.User;
import com.example.springExample.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(Model model){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam String username, String password, String repeat_password, Model model){
        if(!password.equals(repeat_password)){
            model.addAttribute("login",username);
            model.addAttribute("message","Пароли не совпадают");
            return "registration";
        }
        User user = new User(username,password);
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if(userFromDB!=null){
            model.addAttribute("message","Данный пользователь уже зарегистрирован");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}
