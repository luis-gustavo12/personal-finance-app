DELETE FROM payment_methods
WHERE description = 'CREDIT CARD' OR description = 'DEBIT CARD';

INSERT INTO payment_methods (description)
VALUES ('CARD');