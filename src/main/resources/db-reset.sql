-- db-reset.sql
-- Drop all tables if they exist
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS delivery_partners CASCADE;

-- Create delivery_partners table
CREATE TABLE delivery_partners (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
                                   phone_number VARCHAR(50) NOT NULL UNIQUE,
                                   email VARCHAR(255) NOT NULL UNIQUE,
                                   status VARCHAR(20) NOT NULL,
                                   current_city VARCHAR(100) NOT NULL,
                                   vehicle_type VARCHAR(50),
                                   vehicle_number VARCHAR(50),
                                   assigned_order_count INT DEFAULT 0 NOT NULL,
                                   is_active BOOLEAN DEFAULT true NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        order_id VARCHAR(50) NOT NULL UNIQUE,
                        customer_id VARCHAR(255) NOT NULL,
                        customer_name VARCHAR(255) NOT NULL,
                        restaurant_id VARCHAR(255) NOT NULL,
                        restaurant_name VARCHAR(255) NOT NULL,
                        order_type VARCHAR(20) NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        payment_status VARCHAR(20) NOT NULL,
                        delivery_city VARCHAR(100) NOT NULL,
                        restaurant_city VARCHAR(100) NOT NULL,
                        total_amount DECIMAL(10,2),
                        delivery_charge DECIMAL(10,2),
                        tax_amount DECIMAL(10,2),
                        grand_total DECIMAL(10,2),
                        delivery_partner_id VARCHAR(255),
                        estimated_delivery_minutes INT,
                        actual_delivery_minutes INT,
                        special_instructions VARCHAR(500),
                        cancellation_reason VARCHAR(500),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        assigned_at TIMESTAMP,
                        picked_at TIMESTAMP,
                        delivered_at TIMESTAMP,
                        cancelled_at TIMESTAMP,

    -- Indexes for performance
                        INDEX idx_order_id (order_id),
                        INDEX idx_customer_id (customer_id),
                        INDEX idx_restaurant_id (restaurant_id),
                        INDEX idx_status (status),
                        INDEX idx_delivery_city (delivery_city),
                        INDEX idx_created_at (created_at),
                        INDEX idx_delivery_partner (delivery_partner_id)
);

-- Create order_items table
CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             item_id VARCHAR(255) NOT NULL,
                             item_name VARCHAR(255) NOT NULL,
                             quantity INT NOT NULL,
                             unit_price DECIMAL(10,2) NOT NULL,
                             total_price DECIMAL(10,2),

    -- Foreign key constraint
                             FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,

    -- Indexes
                             INDEX idx_order_items_order (order_id),
                             INDEX idx_item_id (item_id)
);

-- Insert fresh sample delivery partners
INSERT INTO delivery_partners (name, phone_number, email, status, current_city, vehicle_type, vehicle_number, assigned_order_count, is_active) VALUES
                                                                                                                                                   ('Rajesh Kumar', '+919876543201', 'rajesh@delivery.com', 'AVAILABLE', 'Bangalore', 'BIKE', 'KA01AB1234', 0, true),
                                                                                                                                                   ('Suresh Patel', '+919876543202', 'suresh@delivery.com', 'AVAILABLE', 'Bangalore', 'BIKE', 'KA02CD5678', 1, true),
                                                                                                                                                   ('Amit Sharma', '+919876543203', 'amit@delivery.com', 'BUSY', 'Bangalore', 'SCOOTER', 'KA03EF9012', 3, true),
                                                                                                                                                   ('Vikram Singh', '+919876543204', 'vikram@delivery.com', 'AVAILABLE', 'Mumbai', 'CAR', 'MH01GH3456', 0, true),
                                                                                                                                                   ('Rahul Verma', '+919876543205', 'rahul@delivery.com', 'OFFLINE', 'Delhi', 'BIKE', 'DL01IJ7890', 0, false),
                                                                                                                                                   ('Priya Nair', '+919876543206', 'priya@delivery.com', 'AVAILABLE', 'Bangalore', 'SCOOTER', 'KA04KL0123', 2, true),
                                                                                                                                                   ('Sanjay Mehta', '+919876543207', 'sanjay@delivery.com', 'AVAILABLE', 'Mumbai', 'BIKE', 'MH02MN4567', 1, true);

-- Insert fresh sample orders
INSERT INTO orders (order_id, customer_id, customer_name, restaurant_id, restaurant_name, order_type, status, payment_status, delivery_city, restaurant_city, total_amount, delivery_charge, tax_amount, grand_total, delivery_partner_id, estimated_delivery_minutes, special_instructions, created_at, updated_at) VALUES
                                                                                                                                                                                                                                                                                                                         ('OMS-2025-0001', 'CUST001', 'John Doe', 'REST001', 'Pizza Hut', 'FOOD', 'CREATED', 'PENDING', 'Bangalore', 'Bangalore', 299.99, 29.99, 5.00, 334.98, NULL, 45, 'Leave at door, no contact delivery', '2025-12-25 10:30:00', '2025-12-25 10:30:00'),
                                                                                                                                                                                                                                                                                                                         ('OMS-2025-0002', 'CUST002', 'Jane Smith', 'REST002', 'Burger King', 'FOOD', 'ASSIGNED', 'COMPLETED', 'Bangalore', 'Bangalore', 199.99, 29.99, 5.00, 234.98, 'dp_001', 30, 'Call before delivery', '2025-12-25 11:15:00', '2025-12-25 11:20:00'),
                                                                                                                                                                                                                                                                                                                         ('OMS-2025-0003', 'CUST003', 'Bob Johnson', 'REST003', 'Big Basket', 'GROCERY', 'DELIVERED', 'COMPLETED', 'Mumbai', 'Mumbai', 599.99, 39.99, 10.00, 649.98, 'dp_004', 60, 'Handle with care', '2025-12-24 09:45:00', '2025-12-24 10:45:00'),
                                                                                                                                                                                                                                                                                                                         ('OMS-2025-0004', 'CUST004', 'Alice Brown', 'REST004', 'Dominos', 'FOOD', 'PICKED', 'COMPLETED', 'Bangalore', 'Bangalore', 399.99, 29.99, 8.00, 437.98, 'dp_002', 35, 'Extra cheese, no onions', '2025-12-25 12:00:00', '2025-12-25 12:15:00'),
                                                                                                                                                                                                                                                                                                                         ('OMS-2025-0005', 'CUST005', 'Charlie Wilson', 'REST001', 'Pizza Hut', 'FOOD', 'ON_THE_WAY', 'COMPLETED', 'Bangalore', 'Bangalore', 249.99, 29.99, 5.00, 284.98, 'dp_006', 40, 'Ring bell twice', '2025-12-25 13:30:00', '2025-12-25 13:45:00'),
                                                                                                                                                                                                                                                                                                                         ('OMS-2025-0006', 'CUST001', 'John Doe', 'REST005', 'KFC', 'FOOD', 'CANCELLED', 'REFUNDED', 'Bangalore', 'Bangalore', 179.99, 29.99, 4.00, 213.98, NULL, 25, 'Customer requested cancellation', '2025-12-24 18:00:00', '2025-12-24 18:10:00'),
                                                                                                                                                                                                                                                                                                                         ('OMS-2025-0007', 'CUST006', 'Emma Watson', 'REST006', 'Apollo Pharmacy', 'PHARMACY', 'CREATED', 'PENDING', 'Delhi', 'Delhi', 89.99, 19.99, 2.00, 111.98, NULL, 20, 'Urgent delivery required', '2025-12-25 14:00:00', '2025-12-25 14:00:00');

-- Insert order items
INSERT INTO order_items (order_id, item_id, item_name, quantity, unit_price, total_price) VALUES
                                                                                              (1, 'ITEM001', 'Margherita Pizza', 2, 149.99, 299.98),
                                                                                              (1, 'ITEM002', 'Garlic Bread', 1, 99.99, 99.99),
                                                                                              (2, 'ITEM003', 'Whopper Burger', 1, 199.99, 199.99),
                                                                                              (2, 'ITEM004', 'French Fries', 2, 79.99, 159.98),
                                                                                              (3, 'ITEM005', 'Milk 1L', 2, 60.00, 120.00),
                                                                                              (3, 'ITEM006', 'Bread', 1, 40.00, 40.00),
                                                                                              (3, 'ITEM007', 'Eggs (12)', 1, 80.00, 80.00),
                                                                                              (3, 'ITEM008', 'Butter 500g', 1, 120.00, 120.00),
                                                                                              (4, 'ITEM009', 'Farmhouse Pizza', 1, 399.99, 399.99),
                                                                                              (5, 'ITEM010', 'Pepperoni Pizza', 1, 249.99, 249.99),
                                                                                              (6, 'ITEM011', 'Chicken Bucket', 1, 179.99, 179.99),
                                                                                              (7, 'ITEM012', 'Paracetamol', 2, 19.99, 39.98),
                                                                                              (7, 'ITEM013', 'Vitamin C', 1, 50.00, 50.00);

-- Update order total amounts based on items
UPDATE orders o
SET total_amount = (
    SELECT SUM(total_price)
    FROM order_items oi
    WHERE oi.order_id = o.id
),
    grand_total = (
        SELECT SUM(total_price) + o.delivery_charge + o.tax_amount
        FROM order_items oi
        WHERE oi.order_id = o.id
    );

-- Display table counts
SELECT 'Delivery Partners' as table_name, COUNT(*) as record_count FROM delivery_partners
UNION ALL
SELECT 'Orders', COUNT(*) FROM orders
UNION ALL
SELECT 'Order Items', COUNT(*) FROM order_items;

-- Display sample data
SELECT '=== DELIVERY PARTNERS ===' as info;
SELECT id, name, status, current_city, assigned_order_count, is_active FROM delivery_partners;

SELECT '=== ORDERS SUMMARY ===' as info;
SELECT
    order_id,
    customer_name,
    restaurant_name,
    status,
    delivery_city,
    grand_total,
    special_instructions
FROM orders
ORDER BY created_at DESC;

SELECT '=== ORDER ITEMS SAMPLE ===' as info;
SELECT
    oi.order_id,
    oi.item_name,
    oi.quantity,
    oi.unit_price,
    oi.total_price,
    o.order_id as external_order_id
FROM order_items oi
         JOIN orders o ON oi.order_id = o.id
    LIMIT 10;