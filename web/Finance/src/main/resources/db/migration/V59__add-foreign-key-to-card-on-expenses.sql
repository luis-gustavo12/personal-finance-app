ALTER TABLE installments
ADD COLUMN card_id BIGINT;

ALTER TABLE installments
ADD CONSTRAINT fk_card_id
FOREIGN KEY (card_id) REFERENCES cards(id);