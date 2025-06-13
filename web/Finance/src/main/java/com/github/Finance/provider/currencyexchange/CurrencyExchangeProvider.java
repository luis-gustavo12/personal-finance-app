package com.github.Finance.provider.currencyexchange;

import java.time.LocalDate;

public interface CurrencyExchangeProvider {


    public Double getExchangeRate(String fromCurrency, String toCurrency, LocalDate date);

}