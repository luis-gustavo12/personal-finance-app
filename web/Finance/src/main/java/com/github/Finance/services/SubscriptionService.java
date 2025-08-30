package com.github.Finance.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.github.Finance.dtos.SubscriptionDetailsDTO;
import com.github.Finance.dtos.subscriptions.SubscriptionsDashboardView;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.dtos.views.SubscriptionsSummaryView;
import com.github.Finance.models.*;
import com.github.Finance.provider.currencyexchange.CurrencyExchangeProvider;
import com.github.Finance.provider.currencyexchange.FrankfurterCurrencyProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.github.Finance.dtos.forms.AddSubscriptionForm;
import com.github.Finance.repositories.SubscriptionRepository;

@Slf4j
@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final AuthenticationService authenticationService;
    private final CurrencyService currencyService;
    private final PaymentMethodsService paymentMethodsService;
    private final ExchangeRateService exchangeRateService;
    private final CardService cardService;
    private final CardSubscriptionDetailsService cardSubscriptionDetailsService;
    private final CategoryService categoryService;


    public SubscriptionService (SubscriptionRepository repository, AuthenticationService authenticationService, CurrencyService currencyService, PaymentMethodsService paymentMethodsService, CurrencyExchangeProvider currencyExchangeProvider, FrankfurterCurrencyProvider frankfurterCurrencyProvider, ExchangeRateService exchangeRateService, CardService cardService, CardSubscriptionDetailsService cardSubscriptionDetailsService, CategoryService categoryService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
        this.currencyService = currencyService;
        this.paymentMethodsService = paymentMethodsService;
        this.exchangeRateService = exchangeRateService;
        this.cardService = cardService;
        this.cardSubscriptionDetailsService = cardSubscriptionDetailsService;
        this.categoryService = categoryService;
    }


    public Subscription createNewSubscription(AddSubscriptionForm form) {


        if (form.dayOfCharging() > 31 || form.dayOfCharging() < 0) {
            throw new RuntimeException("Invalid day of charging");
        }

        Subscription subscription = getSubscriptionFromForm(form);
        return repository.save(subscription);

    }

    private Subscription getSubscriptionFromForm(AddSubscriptionForm form) {
        Subscription subscription = new Subscription();
        subscription.setUser(authenticationService.getCurrentAuthenticatedUser());
        subscription.setName(form.subscriptionName());
        subscription.setCost(form.subscriptionCost());
        subscription.setCurrency( currencyService.findCurrency(form.currencySelect()) );
        subscription.setPaymentMethod( paymentMethodsService.findPaymentMethod(form.subscriptionPaymentForm()) );
        subscription.setDayOfCharging(form.dayOfCharging());
        subscription.setValidFrom(LocalDate.now());
        categoryService.validCategory(form.subscriptionCategory());
        subscription.setCategory(categoryService.getCategoryById(form.subscriptionCategory()));
        return subscription;
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
            subscription.getCategory().getCategoryName(),
            calculateNextCharge(subscription.getDayOfCharging()),
            subscription.getName(),
            subscription.getDayOfCharging()
        );


    }

    public List<CardView> getAuthenticatedUserCards() {
        return cardService.getUserRegisteredCards();
    }

    /**
     * Retrieves all currencies, ordered by the relevance of their expenses
     * @return The ordered list with the currencies
     */
    public List<Currency> getUserCurrencies() {

        User user = authenticationService.getCurrentAuthenticatedUser();
        return currencyService.findAllCurrenciesByUserAndExpense(user);

    }

    private LocalDate calculateNextCharge(Byte dayOfCharging) {

        LocalDate now = LocalDate.now();

        if (dayOfCharging >= now.getDayOfMonth()) {
            return now;
        }

        return now.plusMonths(1);
    }

    public Subscription editSubscription(@Valid AddSubscriptionForm form, Long subscriptionId) {
        Subscription subscription = repository.findById(subscriptionId).orElse(null);

        if (subscription == null) {
            throw new RuntimeException("Invalid subscription id");
        }

        User user = authenticationService.getCurrentAuthenticatedUser();

        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You're not allowed to edit this subscription");
        }

        Subscription subscriptionToEdit = getSubscriptionFromForm(form);

        return repository.save(subscriptionToEdit);


    }

    public void deleteSubscription(Long id) {

        Subscription subscription = repository.findById(id).orElse(null);

        if (subscription == null) {
            throw new RuntimeException("Invalid subscription id");
        }

        User user = authenticationService.getCurrentAuthenticatedUser();

        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You're not allowed to delete this subscription");
        }

        repository.delete(subscription);
        log.info("Delete subscription with id: " + id);

    }

    public List<Subscription> getAllSubscriptionsForToday() {

        return repository.findTodayChargedSubscriptions((byte) LocalDate.now().getDayOfMonth());

    }

    /**
     * Method responsible for giving a nice overview of the subscriptions
     * @param user Desired user
     * @return The View
     */
    public SubscriptionsDashboardView getSubscriptionsOverallSummary(User user) {

        List<Subscription> subscriptions = repository.findSubscriptionsSummary(user);
        log.debug("Found {} subscription(s)", subscriptions.size());

        if (subscriptions.isEmpty()) {
            return null;
        }

        Double totalAmount = subscriptions.stream()
                .map(Subscription::getCost)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        List<String> subscriptionsNames = new ArrayList<>(subscriptions.size());

        for (Subscription subscription : subscriptions) {

            subscriptionsNames.add(
                String.format("%s (%s %s)", subscription.getName(), subscription.getCurrency().getCurrencyFlag() ,subscription.getCost())
            );

        }

        return new SubscriptionsDashboardView(
            subscriptions.size(),
            user.getPreferredCurrency(),
            totalAmount, subscriptionsNames
        );

    }

}
