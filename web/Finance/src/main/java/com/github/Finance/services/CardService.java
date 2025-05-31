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

    public CardService(CardRepository cardRepository, AuthenticationService authenticationService,
    EncryptionService encryptionService ) {
        this.repository = cardRepository;
        this.authenticationService = authenticationService;
        this.encryptionService = encryptionService;
    }


    public List<CardType> getAllCardTypes() {
        return Arrays.asList(CardType.values());
    }

    public CardView addCard(AddCardForm form) {

        Card card = new Card();

        card.setUser(authenticationService.getCurrentAuthenticatedUser());
        card.setCardholderName(form.cardholderName());
        card.setFirstSixDigits( encryptionService.encrypt(form.firstSixDigits()) );
        card.setLastFourDigits( encryptionService.encrypt(form.lastFourDigits()) );
        card.setExpirationMonth(form.expirationMonth());
        card.setExpirationYear(form.expirationYear());
        card.setCardType(CardType.valueOf(form.cardType()));
        card.setBrandName(form.brandName());

        card = repository.save(card);

        return CardMapper.fromEntityToView(card);

    }


    public List<CardView> getUserRegisteredCards() {
        
        User user = authenticationService.getCurrentAuthenticatedUser();

          
        List<Card> cards = repository.findAllByUser(user);
        List<CardView> cardViews = new ArrayList<>(cards.size());

        // Decrypting the stored card digits, to deliver it to the view
        for (Card card : cards) {

            card.setFirstSixDigits(encryptionService.decrypt(card.getFirstSixDigits()));
            card.setLastFourDigits(encryptionService.decrypt(card.getLastFourDigits()));
            cardViews.add( CardMapper.fromEntityToView(card) );

        }
        

        return cardViews;

    }

    public Card getCardByFirstAndLastDigits(String firstDigits, String lastDigits) {
        String encryptedFirstDigits = encryptionService.encrypt(firstDigits);
        String encryptedLastDigits = encryptionService.encrypt(lastDigits);
        return repository.findByFirstSixDigitsAndLastFourDigits(encryptedFirstDigits, encryptedLastDigits).
            orElseThrow(() -> new ResourceNotFoundException("Couldn't find card given the first and last digits!!"));
    }


}
