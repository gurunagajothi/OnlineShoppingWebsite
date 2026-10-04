-- ShopEase – MySQL database script (optional)
-- The application creates all of this automatically on first start.
-- Run this file manually only if you want to create the database yourself:
--     mysql -u root -p < database.sql

CREATE DATABASE IF NOT EXISTS shopease CHARACTER SET utf8mb4;
USE shopease;

CREATE TABLE IF NOT EXISTS products (
    id          INT PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    category    VARCHAR(50)   NOT NULL,
    price       DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    emoji       VARCHAR(16)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS orders (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100)  NOT NULL,
    phone         VARCHAR(15)   NOT NULL,
    address       VARCHAR(500)  NOT NULL,
    total         DECIMAL(10,2) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS order_items (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    order_id     INT           NOT NULL,
    product_id   INT           NOT NULL,
    product_name VARCHAR(100)  NOT NULL,
    quantity     INT           NOT NULL,
    price        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

INSERT IGNORE INTO products (id, name, category, price, description, emoji) VALUES
(1, 'Classic Backpack',     'Bags',        899,  'Strong everyday backpack',            '🎒'),
(2, 'Wireless Headphones',  'Electronics', 1499, 'Bluetooth headphones with clear sound','🎧'),
(3, 'Smart Watch',          'Electronics', 1999, 'Fitness tracking and notifications',  '⌚'),
(4, 'Running Shoes',        'Footwear',    1299, 'Lightweight running shoes',           '👟'),
(5, 'Cotton T-Shirt',       'Fashion',     599,  'Soft cotton regular-fit T-shirt',     '👕'),
(6, 'Travel Bottle',        'Accessories', 399,  'Reusable leak-proof bottle',          '🧴'),
(7, 'Sunglasses',           'Accessories', 699,  'UV-protection sunglasses',            '🕶️'),
(8, 'Laptop Stand',         'Electronics', 999,  'Adjustable laptop stand',             '💻');

-- Useful queries to show the saved data (for screenshots / viva):
-- SELECT * FROM products;
-- SELECT * FROM orders;
-- SELECT * FROM order_items;
