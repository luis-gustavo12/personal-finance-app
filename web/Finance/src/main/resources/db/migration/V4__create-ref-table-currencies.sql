CREATE TABLE currencies (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    currency_name VARCHAR(55) NOT NULL,
    currency_flag VARCHAR(3) NOT NULL UNIQUE,
    currency_symbol VARCHAR(5) NOT NULL,
    decimal_places INT DEFAULT 2
);