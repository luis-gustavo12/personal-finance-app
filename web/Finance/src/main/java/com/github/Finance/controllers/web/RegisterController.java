package com.github.Finance.controllers.web;

import com.github.Finance.models.Currency;
import com.github.Finance.services.CurrencyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.Finance.dtos.forms.RegisterForm;
import com.github.Finance.models.User;
import com.github.Finance.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequestMapping("register")
public class RegisterController {

    private final UserService userService;
    private final CurrencyService currencyService;

    public RegisterController(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
    }

    @GetMapping
    public String registerPage(Model model) {
        List<Currency> currencyList = currencyService.findAllCurrencies();
        model.addAttribute("currencies", currencyList);
        return "register";
    }

    @PostMapping("/submit")
    public String register(RegisterForm form) {

        User newUser = userService.createUser(form);

        return "redirect:/login";
    }
    
    
}
