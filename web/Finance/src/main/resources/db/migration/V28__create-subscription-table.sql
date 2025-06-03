

CREATE TABLE subscriptions (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subscription_name VARCHAR(50) NOT NULL,
    currency_id BIGINT NOT NULL,
    subscription_cost DECIMAL(8, 2) NOT NULL,
    payment_method_id BIGINT NOT NULL,
    recurrence_rule VARCHAR(255),
    valid_from DATE NOT NULL,
    valid_until DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    categories VARCHAR(50) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
);