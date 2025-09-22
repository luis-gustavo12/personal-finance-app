
ALTER TABLE expenses
ADD COLUMN card_id BIGINT NULL;

ALTER TABLE expenses
ADD CONSTRAINT fk_expenses_card_id
FOREIGN KEY (card_id) REFERENCES cards(id);