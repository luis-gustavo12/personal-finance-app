
CREATE TABLE expenses (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    payment_method_id BIGINT,
    currency_id BIGINT,
    amount DECIMAL(8,2) NOT NULL,
    extra_info VARCHAR(255),

    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id)

);