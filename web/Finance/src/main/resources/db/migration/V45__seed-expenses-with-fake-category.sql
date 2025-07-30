UPDATE expenses

SET category_id = (SELECT id FROM categories WHERE category_name = 'Entertainment' LIMIT 1)
WHERE category_id = NULL;