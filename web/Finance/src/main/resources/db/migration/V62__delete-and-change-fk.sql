
ALTER TABLE expenses
    DROP FOREIGN KEY fk_installment_id;


ALTER TABLE expenses
    ADD CONSTRAINT fk_installment_id
        FOREIGN KEY (installment_id) REFERENCES installments(id)
            ON DELETE CASCADE;