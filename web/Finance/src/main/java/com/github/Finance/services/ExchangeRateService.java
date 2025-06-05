package com.github.Finance.services;

import com.github.Finance.dtos.views.SubscriptionsSummaryView;
import com.github.Finance.models.Currency;
import com.github.Finance.models.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExchangeRateService {

    private final CurrencyService currencyService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String EXCHANGE_URL = "https://api.exchangerate.host/historical?date=%s&base=%s&symbols=%s&access_key=%s";

    @Value("${EXCHANGE_RATE_API_KEY}")
    private String apiKey;

    public ExchangeRateService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     *
     * Method responsible for getting all the subscriptions, and bring an estimate calculation
     * of the costs to the user. The idea isn't
     *
     * @param subscriptions - User's subscriptions
     * @return the view containing all the data
     * <p>
     * NOTE: For now, it's running runtime calculations, but it'd be interesting to make this
     * a cache.
     *
     *
     * TODO: make this flow be a cash, and probably, delete it later
     *
     */

    public SubscriptionsSummaryView getSubscriptionsSummary(List<Subscription> subscriptions) {

        Double fullAmount = 0.0;


        for (Subscription subscription : subscriptions) {
            fullAmount += handleRequest(subscription.getCurrency().getCurrencyFlag(), LocalDate.now());
        }

        return new SubscriptionsSummaryView(
                fullAmount, "BRL"
        );

    }

    @Cacheable(value = "exchangeRates", key = "#currencyFlag + '-' + #date")
    public Double handleRequest(String currencyFlag, LocalDate date) {

        String url = String.format(
            EXCHANGE_URL,
            date,
            // Fow now, hardcoding it
            currencyFlag,
            "BRL",
            apiKey
        );

        log.info("Date: {}, Requested Currency: {}", LocalDate.now().toString(), currencyFlag);

        Map<?, ?> response = restTemplate.getForObject(url, Map.class);

        log.info("Response: {}", response);

        // Again, hardcoded for now
        String desiredQuote = currencyFlag + "BRL";

        log.info("Desired Quote: {}", desiredQuote);

        Map<String, Double> rates = (Map<String, Double>) response.get("quotes");

        Double rate = (Double) rates.get(desiredQuote);

        log.info("Rate: {}", rate);

        return rate;

    }


}
