

CREATE TABLE monthly_subscription_card (

    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    monthly_subscription_id BIGINT,
    card_id BIGINT,


    FOREIGN KEY (monthly_subscription_id) REFERENCES monthly_subscriptions(id),
    FOREIGN KEY (card_id) REFERENCES cards(id)

);