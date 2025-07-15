package com.github.Finance.controllers.web;

import com.github.Finance.services.AccountRecoverService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recover")
@Validated
public class AccountRecoverController {

    private final AccountRecoverService accountRecoverService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountRecoverController(AccountRecoverService accountRecoverService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRecoverService = accountRecoverService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("/password")
    public String recoverPasswordForm() {
        return "recover-password";
    }

    @PostMapping("/password")
    public String recoverPasswordEmailSending(@Valid @Email String email) {
        accountRecoverService.generateRecoverToken(email);
        return "redirect:/dashboard";
    }

    @GetMapping("/token/{token}")
    public String recoverPasswordByToken(@PathVariable String token) {
        return "input-new-password";
    }

    @PostMapping("/token/{token}")
    public String resetPassword(@PathVariable String token, String password) {
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        accountRecoverService.recoverAccountByToken(token, hashedPassword);
        return "redirect:/dashboard";
    }


}
