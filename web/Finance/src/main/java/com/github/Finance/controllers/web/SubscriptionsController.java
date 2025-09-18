package com.github.Finance.controllers.web;

import com.github.Finance.models.Subscription;
import com.github.Finance.services.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final CategoryService categoryService;

    public SubscriptionsController(SubscriptionService service, CategoryService categoryService) {
        this.service = service;
        this.categoryService = categoryService;
    }


    @GetMapping("")
    public String subscriptionsDashboard(Model model) {
        model.addAttribute("subscriptions", service.getUserSubscriptions());
        model.addAttribute("summary", service.getUserSubscriptionsSummary());
        return "subscriptions";

    }

    @GetMapping("create")
    public String createSubscription(Model model) {
        
        List<Currency> currencies = service.getUserCurrencies();
        List<PaymentMethod> paymentMethods = service.getPaymentMethods();
        model.addAttribute("currencies", currencies);
        model.addAttribute("methods", paymentMethods);
        model.addAttribute("categories", categoryService.getAllCurrentLoggedUserCategories());
        return "create-subscription";
    }

    @PostMapping("create")
    public String createNewSubscription(@Valid AddSubscriptionForm form) {

        Subscription subscription = service.createNewSubscription(form);
        Long subscriptionId = subscription.getId();

        Long paymentMethodId = subscription.getPaymentMethod().getId();

        // Card payment methods
        if (paymentMethodId == 3 || paymentMethodId == 4) {

            if (service.getAuthenticatedUserCards().isEmpty()) {
                log.info("No cards found, redirecting user to registering a new card!!");
                return "redirect:/cards/create";
            }

            log.info("New credit card subscription has been created, redirecting for filling card details");
            log.info("Route: [{}]", ("card-details/" + subscriptionId ));
            return "redirect:/subscriptions/card-details/" + subscriptionId;
        }

        log.debug("New subscription has been created");
        return "redirect:/subscriptions";
    }

    // You need this id path variable for post mapping, so ignore unused variable warning
    @GetMapping("card-details/{id}")
    public String cardDetails(Model model, @PathVariable("id") Long id) {
        model.addAttribute("cards", service.getUserRegisteredCards());
        return "subscription-card-details";
    }

    @PostMapping("card-details/{id}")
    public String cardDetails(Long selectedCardId, @PathVariable("id") Long id) {
        service.addCardSubscriptionDetailWithSubscription(selectedCardId, id);
        return "redirect:/subscriptions/";
    }

    @GetMapping("/{id}")
    public String subscriptionOverview(Model model, @PathVariable("id") Long id) {
        model.addAttribute("overview", service.getSubscriptionDetail(id));
        return "subscription-overview";
    }

    @GetMapping("/edit/{id}")
    public String subscriptionEdit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("currencies", service.getUserCurrencies());
        model.addAttribute("subscription", service.getSubscriptionDetail(id));
        model.addAttribute("paymentMethods", service.getPaymentMethods());
        model.addAttribute("categories", categoryService.getAllCurrentLoggedUserCategories());
        return "subscription-edit";
    }

    @PostMapping("/edit/{id}")
    public String subscriptionEdit(@Valid AddSubscriptionForm form, @PathVariable("id") Long id) {
        Subscription subscription =  service.editSubscription(form, id);

        return "redirect:/subscriptions/";
    }

    @GetMapping("/delete/{id}")
    public String subscriptionDelete(@PathVariable("id") Long id) {
        service.deleteSubscription(id);
        return "redirect:/subscriptions";
    }


}
