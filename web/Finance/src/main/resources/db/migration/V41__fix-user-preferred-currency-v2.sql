ALTER TABLE users
ADD CONSTRAINT fk_user_currency
FOREIGN KEY (preferred_currency) REFERENCES currencies(id);