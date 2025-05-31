

CREATE TABLE card_expenses (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    expense_id BIGINT UNIQUE,
    card_id BIGINT,
    is_installment BOOLEAN NOT NULL,
    splits INT,
    has_interest_rate BOOLEAN NOT NULL,
    interest_rate DECIMAL(8,2),
    amount_without_interest DECIMAL (8,2),
    status ENUM('PENDING', 'PAID', 'CANCELLED'),
    transaction_date DATE NOT NULL,

    FOREIGN KEY (card_id) REFERENCES cards(id),

    FOREIGN KEY (expense_id) REFERENCES expenses(id)

);