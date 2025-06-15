package com.github.Finance.factories;

import com.github.Finance.provider.cardgateway.CardGatewayProvider;
import com.github.Finance.provider.cardgateway.StripeCardGatewayProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CardGatewayProviderFactory {

    private final String provider;
    private final CardGatewayProvider cardGatewayProvider;

    public CardGatewayProviderFactory(@Value("${cardgateway.provider}") String provider, StripeCardGatewayProvider cardGatewayProvider) {
        this.provider = provider;
        this.cardGatewayProvider = cardGatewayProvider;
    }

    public CardGatewayProvider create() {
        return this.cardGatewayProvider;
    }
}
