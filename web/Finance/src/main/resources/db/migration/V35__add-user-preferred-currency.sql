
ALTER TABLE users
ADD COLUMN preferred_currency BIGINT,
ADD CONSTRAINT fk_user_currency
FOREIGN KEY (preferred_currency) REFERENCES users(id);
