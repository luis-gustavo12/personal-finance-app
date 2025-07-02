package com.github.Finance.dtos;

import com.github.Finance.models.Currency;

public record CurrencyDTO(
    String currencyName,
    String currencyFlag,
    String currencySymbol
)
{

    public CurrencyDTO(Currency currency) {
        this (currency.getCurrencyName(), currency.getCurrencyFlag(), currency.getCurrencySymbol());
    }

}
