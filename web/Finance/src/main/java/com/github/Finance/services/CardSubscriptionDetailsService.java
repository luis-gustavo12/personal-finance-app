package com.github.Finance.services;

import com.github.Finance.models.Card;
import com.github.Finance.models.CardSubscriptionDetails;
import com.github.Finance.models.Subscription;
import com.github.Finance.repositories.CardSubscriptionDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class CardSubscriptionDetailsService {

    private final CardSubscriptionDetailsRepository repository;
    private final CardService cardService;

    public CardSubscriptionDetailsService(CardSubscriptionDetailsRepository repository, CardService cardService) {
        this.repository = repository;
        this.cardService = cardService;
    }

    public CardSubscriptionDetails saveNewCardSubscription(Subscription subscription, Long cardId) {

        Card card = cardService.getCardById(cardId);

        CardSubscriptionDetails subscriptionDetails = new CardSubscriptionDetails();
        subscriptionDetails.setCard(card);
        subscriptionDetails.setSubscription(subscription);

        return repository.save(subscriptionDetails);
    }

}
