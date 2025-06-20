package com.github.Finance.provider.currencyexchange;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;


@Component
@Slf4j
public class FrankfurterCurrencyProvider implements CurrencyExchangeProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Gets the exchange rate for a given date between two currencies.
     *
     * @param fromCurrency The currency you have (e.g., "BRL").
     * @param toCurrency The currency you want (e.g., "USD").
     * @param date The date for the exchange rate (e.g., LocalDate.of(2024, 1, 1)).
     * @return The amount of toCurrency equivalent to one unit of fromCurrency on the given date.
     *
     * Example: getExchangeRate("BRL", "USD", LocalDate.of(2024, 1, 1))
     * returns how many USD one BRL was worth on 2024-01-01.
     */
    @Override
    public Double getExchangeRate(String fromCurrency, String toCurrency, LocalDate date) {

        String url;
        log.info("Base Currency: {}", fromCurrency);
        log.info("Destination Currency: {}", toCurrency);

        String desiredConversion = fromCurrency + toCurrency;
        log.info("Desired Conversion: {}", desiredConversion);


        url = String.format(
        "https://api.frankfurter.dev/v1/%s?base=%s&symbols=%s",
            date,
            fromCurrency, toCurrency
        );

        log.info("URL: {}", url);

        Map response = restTemplate.getForObject(url, Map.class);
        log.info("Response: {}", response);
        Map ratesMap = (Map) response.get("rates");
        Double rate = (Double) ratesMap.get(toCurrency);
        log.info("Returning amount: [{}]", rate);
        return rate;


    }
}
