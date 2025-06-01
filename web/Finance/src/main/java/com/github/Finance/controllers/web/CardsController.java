package com.github.Finance.controllers.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.Finance.dtos.forms.AddCardForm;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.enums.CardType;
import com.github.Finance.services.CardService;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/cards")
public class CardsController {
    

    private final CardService service;

    public CardsController(CardService cardService) {
        this.service = cardService;
    }


    @GetMapping("")
    public String cards(Model model) {
        List<CardView> cardViews = service.getUserRegisteredCards();
        model.addAttribute("cardViews", cardViews);
        return "cards";
    }
    
    @GetMapping("/create")
    public String createCardString(Model model) {
        List<CardType> cardTypes = service.getAllCardTypes();
        model.addAttribute("cardTypes", cardTypes); 
        return "create-card";
    }

    @PostMapping("/create")
    public String createCardForm(AddCardForm form) {
        
        service.addCard(form);
        
        return "/dashboard";
    }
    
    

}
