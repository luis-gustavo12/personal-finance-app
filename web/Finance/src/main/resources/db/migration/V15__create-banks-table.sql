
CREATE TABLE banks (

    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    bank_name VARCHAR(60) NOT NULL COMMENT 'Bank name',
    bank_code VARCHAR(10) NOT NULL COMMENT 'Internal bank name',
    bank_code_type VARCHAR(20),
    country_id BIGINT,
    swift_code VARCHAR(12) NOT NULL COMMENT 'International SWITFT/BIC Code',

    FOREIGN KEY (country_id) REFERENCES countries(id)


);