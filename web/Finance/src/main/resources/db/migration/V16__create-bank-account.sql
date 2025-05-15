

-- Add fields for specific country, if needed!!

CREATE TABLE bank_account (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    bank_id BIGINT,
    account_number VARCHAR(70),
    branch_code VARCHAR(70),



    FOREIGN KEY (bank_id) REFERENCES banks(id),
    FOREIGN KEY (user_id) REFERENCES users(id)

);