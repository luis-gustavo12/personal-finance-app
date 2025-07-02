package com.github.Finance.services;

import java.util.List;

import com.github.Finance.dtos.SubscriptionDetailsDTO;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.dtos.views.SubscriptionsSummaryView;
import com.github.Finance.models.*;
import com.github.Finance.provider.currencyexchange.CurrencyExchangeProvider;
import com.github.Finance.provider.currencyexchange.FrankfurterCurrencyProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.github.Finance.dtos.forms.AddSubscriptionForm;
import com.github.Finance.repositories.SubscriptionRepository;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final AuthenticationService authenticationService;
    private final CurrencyService currencyService;
    private final PaymentMethodsService paymentMethodsService;
    private final ExchangeRateService exchangeRateService;
    private final CardService cardService;
    private final CardSubscriptionDetailsService cardSubscriptionDetailsService;


    public SubscriptionService (SubscriptionRepository repository, AuthenticationService authenticationService, CurrencyService currencyService, PaymentMethodsService paymentMethodsService, CurrencyExchangeProvider currencyExchangeProvider, FrankfurterCurrencyProvider frankfurterCurrencyProvider, ExchangeRateService exchangeRateService, CardService cardService, CardSubscriptionDetailsService cardSubscriptionDetailsService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
        this.currencyService = currencyService;
        this.paymentMethodsService = paymentMethodsService;
        this.exchangeRateService = exchangeRateService;
        this.cardService = cardService;
        this.cardSubscriptionDetailsService = cardSubscriptionDetailsService;
    }


    public Subscription createNewSubscription(AddSubscriptionForm form) {

        Subscription subscription = new Subscription();
        subscription.setUser(authenticationService.getCurrentAuthenticatedUser());
        subscription.setName(form.subscriptionName());
        subscription.setCost(form.subscriptionCost());
        subscription.setCurrency( currencyService.findCurrency(form.currencySelect()) );
        subscription.setPaymentMethod( paymentMethodsService.findPaymentMethod(form.subscriptionPaymentForm()) );
        subscription.setValidFrom(form.subscriptionStart());
        subscription.setCategories(form.subscriptionCategory());

        return repository.save(subscription);

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


    public List<CardView> getUserRegisteredCards() {
        return cardService.getUserRegisteredCards();
    }

    public Subscription getSubscriptionById(Long subscriptionId) {
        return repository.findById(subscriptionId).orElse(null);
    }

    /**
     *
     *
     * method that links the subscription with the card details
     *
     * @param cardId The card that links to the subscription
     * @param subscriptionId Subscription id
     */
    @PreAuthorize("@cardService.getCardById(#cardId).getUser().getEmail() == authentication.name &&" +
    "@subscriptionService.getSubscriptionById(#subscriptionId).getUser().getEmail() == authentication.name")
    public void addCardSubscriptionDetailWithSubscription(Long cardId, Long subscriptionId) {
        Subscription subscription = getSubscriptionById(subscriptionId);
        cardSubscriptionDetailsService.saveNewCardSubscription(subscription, cardId);
    }

    @PreAuthorize("@subscriptionService.getSubscriptionById(#subscriptionId).getUser().getEmail() == authentication.name")
    public SubscriptionDetailsDTO getSubscriptionDetail(Long subscriptionId) {

        Subscription subscription = getSubscriptionById(subscriptionId);

        return new SubscriptionDetailsDTO(
            subscription.getCurrency(),
            subscription.getCost().doubleValue(),
            subscription.getPaymentMethod(),
            subscription.getValidUntil() == null ? "Active" : "Not Active",
            subscription.getCreatedAt(),
            subscription.getValidFrom(),
            subscription.getCategories()
        );


    }

    public List<CardView> getAuthenticatedUserCards() {
        return cardService.getUserRegisteredCards();
    }

}
