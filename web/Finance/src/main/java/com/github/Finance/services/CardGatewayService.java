package com.github.Finance.services;

import com.github.Finance.dtos.views.CardView;
import com.github.Finance.factories.CardGatewayProviderFactory;
import com.github.Finance.provider.cardgateway.CardGatewayProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class CardGatewayService {

    private final CardGatewayProviderFactory providerFactory;
    private CardGatewayProvider provider;

    public CardGatewayService(CardGatewayProviderFactory providerFactory) {
        this.providerFactory = providerFactory;
    }

    @PostConstruct
    public void init() {
        this.provider = providerFactory.create();
    }


    public String getPublicToken() {
        return provider.getPublicKey();
    }

    public void getCardDetails(CardView cardView, String token) {
        provider.getCardDetails(cardView, token);
    }


}
