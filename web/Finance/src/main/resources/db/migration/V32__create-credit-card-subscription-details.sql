

CREATE TABLE card_subscription_details (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    subscription_id BIGINT NOT NULL,
    card_id BIGINT NOT NULL,

    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id),
    FOREIGN KEY (card_id) REFERENCES cards(id)


);