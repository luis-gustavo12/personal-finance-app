
ALTER TABLE expenses
ADD CONSTRAINT fk_expense
FOREIGN KEY (subscription_id) REFERENCES subscriptions(id);