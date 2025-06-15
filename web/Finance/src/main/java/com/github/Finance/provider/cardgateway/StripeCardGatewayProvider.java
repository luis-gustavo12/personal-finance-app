package com.github.Finance.provider.cardgateway;


import com.github.Finance.dtos.views.CardView;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StripeCardGatewayProvider implements CardGatewayProvider {

    private final String publicKey;
    private final String secretKey;

    public StripeCardGatewayProvider(@Value("${cardgateway.public-key}") String publicKey, @Value("${cardgateway.private-key}") String privateKey) {
        this.publicKey = publicKey;
        this.secretKey = privateKey;
        Stripe.apiKey = privateKey;
    }

    @Override
    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public void getCardDetails(CardView cardView, String cardToken) {

        log.debug("Stripe Card Gateway Provider - getCardDetails begin");

        try {
            Token token = Token.retrieve(cardToken);
            Card card = token.getCard();
            cardView.setLastDigits(card.getLast4());
            cardView.setBrand(card.getBrand());
            cardView.setCardName(card.getName());
            cardView.setExpirationMonth( card.getExpMonth().intValue());
            cardView.setExpirationYear(card.getExpYear().intValue());



        } catch (StripeException e) {
            log.error("Error with getting Stripe Token: [{}]", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
