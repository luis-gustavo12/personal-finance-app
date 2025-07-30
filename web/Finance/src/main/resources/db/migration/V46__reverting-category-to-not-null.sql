ALTER TABLE expenses
MODIFY COLUMN category_id BIGINT NOT NULL;


ALTER TABLE expenses
ADD CONSTRAINT fk_category_not_null
FOREIGN KEY (category_id) REFERENCES categories(id);