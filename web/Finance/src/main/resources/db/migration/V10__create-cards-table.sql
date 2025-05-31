
CREATE TABLE cards (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    first_six_digits VARCHAR(6) NOT NULL COMMENT 'This field must be encrypted!!!',
    last_four_digits VARCHAR(4) NOT NULL COMMENT 'This field must be encrypted!!!',
    cardholder_name VARCHAR(255) NOT NULL,
    expiration_month INT NOT NULL,
    expiration_year INT NOT NULL,
    card_type ENUM('DEBIT', 'CREDIT') NOT NULL,
    brand_name VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,


    FOREIGN KEY (user_id) REFERENCES users(id)
);