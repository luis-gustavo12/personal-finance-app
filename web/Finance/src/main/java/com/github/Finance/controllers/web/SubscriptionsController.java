package com.github.Finance.controllers.web;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.Finance.dtos.forms.AddSubscriptionForm;
import com.github.Finance.models.Currency;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.services.SubscriptionService;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/subscriptions")
@Slf4j
public class SubscriptionsController {
    
    private final SubscriptionService service;

    public SubscriptionsController(SubscriptionService service) {
        this.service = service;
    }


    @GetMapping("")
    public String subscriptionsDashboard() {

        return "subscriptions";

    }

    @GetMapping("create")
    public String createSubscription(Model model) {
        
        List<Currency> currencies = service.getCurrencies();
        List<PaymentMethod> paymentMethods = service.getPaymentMethods();
        model.addAttribute("currencies", currencies);
        model.addAttribute("methods", paymentMethods);
        return "create-subscription";
    }

    @PostMapping("create")
    public String createNewSubscription(AddSubscriptionForm form) {
        
        service.createNewSubscription(form);
        
        return "dashboard";
    }
    
    

    
}
