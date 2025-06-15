
CREATE TABLE banks (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    bank_name VARCHAR(60) NOT NULL COMMENT 'Full legal name of the financial institution',
    bank_code VARCHAR(10) NOT NULL COMMENT 'Local or national bank identifier',
    bank_code_type ENUM('ABA', 'SWIFT', 'IBAN', 'SORT_CODE') COMMENT 'The standard to which the bank_code belongs',
    country_id BIGINT NOT NULL,
    swift_code VARCHAR(12) NOT NULL COMMENT 'Unique SWIFT/BIC code for international transfers',
    UNIQUE KEY uk_swift_code (swift_code),
    FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE RESTRICT ON UPDATE CASCADE
);