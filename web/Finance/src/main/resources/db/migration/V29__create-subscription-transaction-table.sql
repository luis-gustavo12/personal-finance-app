

CREATE TABLE subscription_transactions(

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    subscription_id BIGINT NOT NULL,
    payment_method_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    amount_paid DECIMAL(8, 2) NOT NULL,
    payment_date DATE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id)

);