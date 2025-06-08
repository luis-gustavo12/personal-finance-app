package com.github.Finance.services;

import java.util.List;

import com.github.Finance.dtos.views.SubscriptionsSummaryView;
import org.springframework.stereotype.Service;

import com.github.Finance.dtos.forms.AddSubscriptionForm;
import com.github.Finance.models.Currency;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.models.Subscription;
import com.github.Finance.models.User;
import com.github.Finance.repositories.SubscriptionRepository;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final AuthenticationService authenticationService;
    private final CurrencyService currencyService;
    private final PaymentMethodsService paymentMethodsService;
    private final ExchangeRateService exchangeRateService;

    public SubscriptionService (SubscriptionRepository repository, AuthenticationService authenticationService, CurrencyService currencyService, PaymentMethodsService paymentMethodsService, ExchangeRateService exchangeRateService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
        this.currencyService = currencyService;
        this.paymentMethodsService = paymentMethodsService;
        this.exchangeRateService = exchangeRateService;
    }


    public void createNewSubscription(AddSubscriptionForm form) {

        Subscription subscription = new Subscription();
        subscription.setUser(authenticationService.getCurrentAuthenticatedUser());
        subscription.setName(form.subscriptionName());
        subscription.setCost(form.subscriptionCost());
        subscription.setCurrency( currencyService.findCurrency(form.currencySelect()) );
        subscription.setPaymentMethod( paymentMethodsService.findPaymentMethod(form.subscriptionPaymentForm()) );
        subscription.setValidFrom(form.subscriptionStart());
        subscription.setCategories(form.subscriptionCategory());

        repository.save(subscription);

    }

    public List<Currency> getCurrencies() {
        return currencyService.findAllCurrencies();
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethodsService.getAllPaymentMethods();
    }

    public List<Subscription> getUserSubscriptions() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        return repository.findAllByUser(user);
    }

    public SubscriptionsSummaryView getUserSubscriptionsSummary() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        List<Subscription> subscriptions = repository.findAllByUser(user);
        return exchangeRateService.getSubscriptionsSummary(subscriptions);
    }

    
}
