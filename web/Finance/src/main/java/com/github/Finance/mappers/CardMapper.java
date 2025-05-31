package com.github.Finance.mappers;


import com.github.Finance.dtos.views.CardView;
import com.github.Finance.models.Card;

public class CardMapper {
    

    public static CardView fromEntityToView(Card card) {

        return new CardView(
            card.getCardholderName(),
            card.getExpirationMonth(),
            card.getExpirationYear(),
            card.getFirstSixDigits(),
            card.getLastFourDigits(),
            card.getBrandName(),
            card.getCardType()
        );

    }

}
