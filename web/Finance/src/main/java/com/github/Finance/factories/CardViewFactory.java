package com.github.Finance.factories;

import com.github.Finance.dtos.views.CardView;
import com.github.Finance.models.Card;
import com.github.Finance.services.CardGatewayService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardViewFactory {

    private final CardGatewayService cardGatewayService;

    public CardViewFactory(CardGatewayService cardGatewayService) {
        this.cardGatewayService = cardGatewayService;
    }

    public CardView create(Card card) {
        CardView cardView = new CardView();

        cardView.setId(card.getId());
        cardView.setCardType(card.getCardType());
        cardView.setCardName(card.getCardDescription());

        cardGatewayService.getCardDetails(cardView, card.getToken());

        return cardView;
    }

    public List<CardView> createList(List<Card> cards) {
        return cards.stream()
                .map(this::create)
                .collect(Collectors.toList());
    }
}