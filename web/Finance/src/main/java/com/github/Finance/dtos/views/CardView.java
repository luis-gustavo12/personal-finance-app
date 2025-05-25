package com.github.Finance.dtos.views;

import java.time.LocalDate;

import com.github.Finance.enums.CardType;


public record CardView (
    String cardNumber,
    String cardName,
    int expirationMonth,
    int expirationYear,
    String brand,
    CardType cardType
)
{
    
}
