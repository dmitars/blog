package com.example.springExample.controllers;

import com.example.springExample.models.User;
import com.example.springExample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult,
                          String repeat_password, Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("message", ControllerUtils.getStringOfErrors(bindingResult));
            return "registration";
        }
        if(!user.getPassword().equals(repeat_password)){
            model.addAttribute("login",user.getUsername());
            model.addAttribute("message","Пароли не совпадают");
            return "registration";
        }
        if(!userService.addUser(user)){
            model.addAttribute("message","Данный пользователь уже зарегистрирован");
            return "registration";
        }
        return "redirect:/login";
    }
}
