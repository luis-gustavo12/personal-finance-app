
CREATE TABLE reports (
    user_id BIGINT NOT NULL,
    currency BIGINT NOT NULL,
    incomes_sum DECIMAL(8, 2) NOT NULL,
    expenses_sum DECIMAL(8, 2) NOT NULL,
    generation_date DATETIME NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (currency) REFERENCES currencies(id)

)


;