-- 1. Increase recursion depth to allow 10,000 rows (MySQL default limit is 1,000)
SET SESSION cte_max_recursion_depth = 10001;

-- 2. Insert Data
-- Note: We exclude 'id' because your Entity uses GenerationType.IDENTITY
INSERT INTO products (name, description, price, category)
WITH RECURSIVE data_generator (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM data_generator WHERE n < 10000
)
SELECT
    -- 1. NAME: Random Adjective + Noun (e.g., "Smart Watch", "Premium Backpack")
    CONCAT(
            ELT(FLOOR(RAND() * 10) + 1, 'Smart', 'Ergonomic', 'Wireless', 'Premium', 'Portable', 'Vintage', 'Sleek', 'Compact', 'Durable', 'Professional'),
            ' ',
            ELT(FLOOR(RAND() * 10) + 1, 'Watch', 'Coffee Maker', 'Headphones', 'Running Shoes', 'Desk Lamp', 'Backpack', 'Thermostat', 'Gaming Mouse', 'Water Bottle', 'Yoga Mat')
    ) AS name,

    -- 2. DESCRIPTION: Simple unique text
    CONCAT('A high-quality item perfect for daily use. Batch #', n) AS description,

    -- 3. PRICE: Random value between 10.00 and 500.00
    ROUND((RAND() * 490 + 10), 2) AS price,

    -- 4. CATEGORY: Random selection from 5 fixed categories
    ELT(FLOOR(RAND() * 5) + 1, 'Electronics', 'Home & Kitchen', 'Sports', 'Fashion', 'Office') AS category

FROM data_generator;