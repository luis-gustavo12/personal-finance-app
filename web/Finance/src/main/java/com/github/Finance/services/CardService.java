package com.github.Finance.services;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.Finance.dtos.forms.AddCardForm;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.enums.CardType;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.mappers.CardMapper;
import com.github.Finance.models.Card;
import com.github.Finance.models.User;
import com.github.Finance.repositories.CardRepository;

@Service
public class CardService {
    

    private final CardRepository repository;
    private final AuthenticationService authenticationService;
    private final EncryptionService encryptionService;
    private final CardGatewayService cardGatewayService;

    public CardService(CardRepository cardRepository, AuthenticationService authenticationService,
            EncryptionService encryptionService, CardGatewayService cardGatewayService) {
        this.repository = cardRepository;
        this.authenticationService = authenticationService;
        this.encryptionService = encryptionService;
        this.cardGatewayService = cardGatewayService;
    }


    public List<CardType> getAllCardTypes() {
        return Arrays.asList(CardType.values());
    }

    public Card addCard(AddCardForm form) {

        Card card = new Card();

        card.setUser(authenticationService.getCurrentAuthenticatedUser());
        card.setToken(form.stripeToken());
        card.setCardType(CardType.valueOf(form.cardType()));
        card.setCardDescription(form.cardDescription());
        return repository.save(card);

    }


    public List<CardView> getUserRegisteredCards() {
        
        User user = authenticationService.getCurrentAuthenticatedUser();

          
        List<Card> cards = repository.findAllByUser(user);
        List<CardView> cardViews = new ArrayList<>(cards.size());

        // Decrypting the stored card digits, to deliver it to the view
        for (Card card : cards) {

            CardView cardView = new CardView();
            cardView.setCardType(card.getCardType());
            cardGatewayService.getCardDetails(cardView, card.getToken());
            cardView.setId(card.getId());
            cardView.setCardName(card.getCardDescription());
            cardViews.add(cardView);
        }
        

        return cardViews;

    }

    public Card getCardByFirstAndLastDigits(String firstDigits, String lastDigits) {
        String encryptedFirstDigits = encryptionService.encrypt(firstDigits);
        String encryptedLastDigits = encryptionService.encrypt(lastDigits);
//        return repository.findByFirstSixDigitsAndLastFourDigits(encryptedFirstDigits, encryptedLastDigits).
//            orElseThrow(() -> new ResourceNotFoundException("Couldn't find card given the first and last digits!!"));

        return null;
    }

    public Card getCardById(Long cardId) {
        return repository.findById(cardId).orElse(null);
    }


    public Card findCardById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
