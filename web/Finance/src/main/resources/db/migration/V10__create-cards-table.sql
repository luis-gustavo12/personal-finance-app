
CREATE TABLE cards (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL ,
    card_type ENUM('DEBIT', 'CREDIT', 'HYBRID', 'PREPAID', 'VIRTUAL', 'GIFT') NOT NULL,
    token VARCHAR(255) NOT NULL,
    card_description VARCHAR(40) COMMENT 'A small card description',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,


    FOREIGN KEY (user_id) REFERENCES users(id)
);