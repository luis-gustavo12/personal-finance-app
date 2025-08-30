ALTER TABLE installments
ADD CONSTRAINT fk_currency_id
FOREIGN KEY (currency_id)
REFERENCES currencies(id)