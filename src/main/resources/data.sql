-- data.sql
-- Insert sample delivery partners
INSERT INTO delivery_partners (name, phone_number, email, status, current_city, current_street, vehicle_type, vehicle_number, assigned_order_count, is_active) VALUES
                                                                                                                                                                   ('Rajesh Kumar', '+919876543201', 'rajesh@delivery.com', 'AVAILABLE', 'Bangalore', 'MG Road', 'BIKE', 'KA01AB1234', 0, true),
                                                                                                                                                                   ('Suresh Patel', '+919876543202', 'suresh@delivery.com', 'AVAILABLE', 'Bangalore', 'Indiranagar', 'BIKE', 'KA02CD5678', 1, true),
                                                                                                                                                                   ('Amit Sharma', '+919876543203', 'amit@delivery.com', 'BUSY', 'Bangalore', 'Koramangala', 'SCOOTER', 'KA03EF9012', 3, true),
                                                                                                                                                                   ('Vikram Singh', '+919876543204', 'vikram@delivery.com', 'AVAILABLE', 'Mumbai', 'Colaba', 'CAR', 'MH01GH3456', 0, true),
                                                                                                                                                                   ('Rahul Verma', '+919876543205', 'rahul@delivery.com', 'OFFLINE', 'Delhi', 'Connaught Place', 'BIKE', 'DL01IJ7890', 0, false);

-- Insert sample orders
INSERT INTO orders (customer_id, customer_name, restaurant_id, restaurant_name, order_type, status, payment_status, delivery_city, restaurant_city, total_amount, delivery_charge, tax_amount, grand_total, delivery_partner_id, estimated_delivery_minutes, special_instructions, created_at, updated_at) VALUES
('CUST001', 'John Doe', 'REST001', 'Pizza Hut', 'FOOD', 'CREATED', 'PENDING', 'Bangalore', 'Bangalore', 299.99, 29.99, 5.00, 334.98, NULL, 45, 'Leave at door, no contact delivery', '2025-12-25 10:30:00', '2025-12-25 10:30:00'),
('CUST002', 'Jane Smith', 'REST002', 'Burger King', 'FOOD', 'ASSIGNED', 'COMPLETED', 'Bangalore', 'Bangalore', 199.99, 29.99, 5.00, 234.98, 1, 30, 'Call before delivery', '2025-12-25 11:15:00', '2025-12-25 11:20:00'),
('CUST003', 'Bob Johnson', 'REST003', 'Big Basket', 'GROCERY', 'DELIVERED', 'COMPLETED', 'Mumbai', 'Mumbai', 599.99, 39.99, 10.00, 649.98, 4, 60, 'Handle with care', '2025-12-24 09:45:00', '2025-12-24 10:45:00');

-- Insert sample order items
INSERT INTO order_items (order_id, item_id, item_name, quantity, unit_price, total_price) VALUES
(1, 'ITEM001', 'Margherita Pizza', 2, 149.99, 299.98),
(1, 'ITEM002', 'Garlic Bread', 1, 99.99, 99.99),
(2, 'ITEM003', 'Whopper Burger', 1, 199.99, 199.99),
(2, 'ITEM004', 'French Fries', 2, 79.99, 159.98),
(3, 'ITEM005', 'Milk 1L', 2, 60.00, 120.00),
(3, 'ITEM006', 'Bread', 1, 40.00, 40.00),
(3, 'ITEM007', 'Eggs (12)', 1, 80.00, 80.00),
(3, 'ITEM008', 'Butter 500g', 1, 120.00, 120.00);