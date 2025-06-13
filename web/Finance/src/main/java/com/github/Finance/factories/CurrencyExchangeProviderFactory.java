package com.github.Finance.factories;

import com.github.Finance.provider.currencyexchange.CurrencyExchangeProvider;
import com.github.Finance.provider.currencyexchange.FrankfurterCurrencyProvider;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class CurrencyExchangeProviderFactory {



    public static CurrencyExchangeProvider create() {

        // For now, since we're only using one API, Frankfurter will
        // be used. The idea is that there'll
        // be a parameter that handles which API to use
        return new FrankfurterCurrencyProvider();

    }


}
