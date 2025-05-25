package com.github.Finance.services;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.Finance.dtos.forms.AddCardForm;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.enums.CardType;
import com.github.Finance.mappers.CardMapper;
import com.github.Finance.models.Card;
import com.github.Finance.models.User;
import com.github.Finance.repositories.CardRepository;

@Service
public class CardService {
    

    private final CardRepository repository;
    private final AuthenticationService authenticationService;

    public CardService(CardRepository cardRepository, AuthenticationService authenticationService) {
        this.repository = cardRepository;
        this.authenticationService = authenticationService;
    }


    public List<CardType> getAllCardTypes() {
        return Arrays.asList(CardType.values());
    }

    public CardView addCard(AddCardForm form) {

        Card card = new Card();

        card.setUser(authenticationService.getCurrentAuthenticatedUser());
        card.setCardNumber(form.cardNumber());
        card.setCardholderName(form.cardholderName());
        card.setExpirationMonth(form.expirationMonth());
        card.setExpirationYear(form.expirationYear());
        card.setCardType(CardType.valueOf(form.cardType()));
        card.setBrandName(form.brandName());

        card = repository.save(card);

        return CardMapper.fromEntityToView(card);

    }


    public List<CardView> getUserRegisteredCards() {
        
        User user = authenticationService.getCurrentAuthenticatedUser();
        List<CardView> cards = repository.findAllByUser(user)
            .stream()
            .map(CardMapper::fromEntityToView)
            .collect(Collectors.toList());
        

        return cards;

    }


}
