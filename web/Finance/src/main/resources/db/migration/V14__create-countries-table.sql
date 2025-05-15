
CREATE TABLE countries (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(60) NOT NULL,
    currency_id BIGINT,
    iso_code_2 CHAR(2) NOT NULL,
    iso_code_3 CHAR(3) NOT NULL,

    FOREIGN KEY (currency_id) REFERENCES currencies(id)

);