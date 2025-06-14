package com.github.Finance.services;

import com.github.Finance.dtos.views.SubscriptionsSummaryView;
import com.github.Finance.factories.CurrencyExchangeProviderFactory;
import com.github.Finance.models.Subscription;
import com.github.Finance.provider.currencyexchange.CurrencyExchangeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ExchangeRateService {

    private final CurrencyExchangeProvider provider = CurrencyExchangeProviderFactory.create();


    public Double getExchangeRate(String currencyFlag, String userCurrency, LocalDate incomeDate) {
        return provider.getExchangeRate(currencyFlag, userCurrency, incomeDate);
    }


    /**
     *
     * Method responsible for getting a sum of the subscriptions cost, in the users' preferred currency
     *
     * @param subscriptions A list of all the subscriptions
     * @return The summary view, basically a Map, of currency and cost
     */
    public SubscriptionsSummaryView getSubscriptionsSummary(List<Subscription> subscriptions) {

        double totalAmount = 0.0;
        String currency = "BRL";


        for (Subscription subscription : subscriptions) {



            if (!subscription.getCurrency().getCurrencyFlag().equals(currency)) {
                Double dailyQuotation = provider.getExchangeRate(currency, subscription.getCurrency().getCurrencyFlag(), LocalDate.now());
                log.info("1 unit of {} equals {}{}", currency, subscription.getCurrency().getCurrencySymbol() ,dailyQuotation);
                totalAmount += (dailyQuotation * subscription.getCost().doubleValue());
            }

            else
                totalAmount += subscription.getCost().doubleValue();

            log.info("Total amount: {}", totalAmount);

        }

        log.info("Final total amount: {}", totalAmount);
        return new SubscriptionsSummaryView(totalAmount, "BRL");

    }
}
