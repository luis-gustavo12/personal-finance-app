package com.github.Finance.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.Finance.dtos.forms.RegisterForm;
import com.github.Finance.models.User;
import com.github.Finance.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registerPage() {
        return "register";
    }

    @PostMapping("/submit")
    public String register(RegisterForm form) {

        User newUser = userService.createUser(form);

        return "redirect:/login";
    }
    
    
}
