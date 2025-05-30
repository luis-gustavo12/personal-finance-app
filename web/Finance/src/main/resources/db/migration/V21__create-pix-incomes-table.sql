
CREATE TABLE pix_incomes(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    income_id BIGINT,
    sender_bank_code BIGINT NOT NULL,
    sender_name VARCHAR(150) NOT NULL,
    sender_document VARCHAR(22) NOT NULL,
    sender_pix_key VARCHAR(25) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    end_to_end_id VARCHAR(255) NOT NULL COMMENT 'ID generated by Government',
    extra_info VARCHAR(80) NOT NULL,

    
    FOREIGN KEY (income_id) REFERENCES incomes(id)

);