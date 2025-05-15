
CREATE TABLE cash_incomes (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    income_id BIGINT,
    note VARCHAR(100),

    FOREIGN KEY (income_id) REFERENCES incomes(id)

);