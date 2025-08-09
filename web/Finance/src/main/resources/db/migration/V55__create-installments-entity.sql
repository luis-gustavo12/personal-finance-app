CREATE TABLE installments(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    amount DECIMAL(8, 2) NOT NULL,
    splits INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    payment_method_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
)