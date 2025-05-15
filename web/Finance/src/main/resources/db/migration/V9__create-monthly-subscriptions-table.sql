CREATE TABLE monthly_subscriptions (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    subscription_name VARCHAR(50) NOT NULL,
    user_id BIGINT,
    type BIGINT,
    currency_id BIGINT,
    price DECIMAL(8,2) NOT NULL,
    payment_method_id BIGINT,
    billing_date DATE NOT NULL,
    subscription_status_id BIGINT,
    notes VARCHAR(150),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (type) REFERENCES subscriptions_type(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id),
    FOREIGN KEY (subscription_status_id) REFERENCES subscription_status(id)



);