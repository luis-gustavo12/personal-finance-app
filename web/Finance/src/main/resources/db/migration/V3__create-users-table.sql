
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    user_status BIGINT,
    role BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    first_name VARCHAR(120) NOT NULL,
    last_name VARCHAR(120) NOT NULL,
    password_valid_until DATETIME COMMENT 'Not null only for regular users, not admins',
    FOREIGN KEY (user_status) REFERENCES user_status(id),
    FOREIGN KEY (role) REFERENCES roles(id)
);