package com.github.Finance.controllers.web;

import java.util.List;

import com.github.Finance.services.CardGatewayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.Finance.dtos.forms.AddCardForm;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.enums.CardType;
import com.github.Finance.services.CardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cards")
public class CardsController {


    private final CardService service;
    private final CardGatewayService cardGatewayService;

    public CardsController(CardService cardService, CardGatewayService cardGatewayService) {
        this.service = cardService;
        this.cardGatewayService = cardGatewayService;
    }


    @GetMapping("")
    public String cards(Model model) {
        List<CardView> cardViews = service.getUserRegisteredCards();
        model.addAttribute("cardViews", cardViews);
        return "cards";
    }
    
    @GetMapping("/create")
    public String createCardString(Model model, @RequestParam(name = "redirectUrl", required = false) String redirectUrl) {
        List<CardType> cardTypes = service.getAllCardTypes();
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("publicKey", cardGatewayService.getPublicToken());
        model.addAttribute("cardTypes", cardTypes); 
        return "create-card";
    }

    @PostMapping("/create")
    public String createCardForm(@Valid AddCardForm form, @RequestParam(name = "redirectUrl", required = false) String redirectUrl) {
        
        service.addCard(form);

        if (!redirectUrl.isEmpty()) {
            return "redirect:" + redirectUrl;
        }
        
        return "/dashboard";
    }
    
    

}
