
CREATE TABLE cash_expense (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    expense_id BIGINT,
    extra_note VARCHAR(100),

    FOREIGN KEY (expense_id) REFERENCES expenses(id)

);
