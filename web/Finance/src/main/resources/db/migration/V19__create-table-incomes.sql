
CREATE TABLE incomes (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    currency_code BIGINT,
    amount DECIMAL(8,2) NOT NULL,
    payment_method_id BIGINT,


    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id),
    FOREIGN KEY (currency_code) REFERENCES currencies(id)
)