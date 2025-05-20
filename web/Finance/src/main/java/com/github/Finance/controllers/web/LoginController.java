package com.github.Finance.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.github.Finance.services.AuthenticationService;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController (AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping({"", "/"})
    public String login() {
        return "login";
    }

    // @PostMapping("/submit")
    // public String loginRequest(LoginForm loginForm) {

    //     System.out.println("HERHERHEHREHREHRE");
        
    //     if (authenticationService.authenticateUser(loginForm)) {
    //         return "home";
    //     }
        
    //     return "redirect:/login?error=true";
    // }
    
    
}
