package com.github.Finance.dtos.views;

import java.time.LocalDate;

import com.github.Finance.enums.CardType;


public record CardView (
    String cardName,
    int expirationMonth,
    int expirationYear,
    String firstDigits,
    String lastDigits,
    String brand,
    CardType cardType
)
{
    
}
