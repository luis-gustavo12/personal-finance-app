CREATE TABLE bank_accounts (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    bank_id BIGINT NOT NULL,
    account_number VARCHAR(70) NOT NULL,
    branch_code VARCHAR(70) COMMENT 'Specific branch identifier, e.g.: Brazilian Agency',
    FOREIGN KEY (bank_id) REFERENCES banks(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uk_bank_branch_account (bank_id, branch_code, account_number)
);