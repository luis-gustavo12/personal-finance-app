package com.github.Finance.provider.cardgateway;

import com.github.Finance.dtos.views.CardView;

public interface CardGatewayProvider {


    public String getPublicKey();
    public void getCardDetails(CardView cardView, String cardToken);

}
