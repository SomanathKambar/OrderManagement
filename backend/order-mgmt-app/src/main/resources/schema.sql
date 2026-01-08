-- schema.sql
-- Drop tables if they exist
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS delivery_partners;

-- Create delivery_partners table with H2 compatible syntax
CREATE TABLE delivery_partners (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   name VARCHAR(100) NOT NULL,
                                   phone_number VARCHAR(20) NOT NULL UNIQUE,
                                   email VARCHAR(100) NOT NULL UNIQUE,
                                   status VARCHAR(20) NOT NULL,

    -- Current Location
                                   current_street VARCHAR(200),
                                   current_city VARCHAR(50) NOT NULL,
                                   current_state VARCHAR(50),
                                   current_postal_code VARCHAR(20),
                                   current_country VARCHAR(50),
                                   current_latitude DOUBLE,
                                   current_longitude DOUBLE,

                                   vehicle_type VARCHAR(20),
                                   vehicle_number VARCHAR(20),
                                   assigned_order_count INT DEFAULT 0 NOT NULL,
                                   is_active BOOLEAN DEFAULT true NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        customer_id VARCHAR(50) NOT NULL,
                        customer_name VARCHAR(100) NOT NULL,
                        restaurant_id VARCHAR(50) NOT NULL,
                        restaurant_name VARCHAR(100) NOT NULL,
                        order_type VARCHAR(20) NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        payment_status VARCHAR(20) NOT NULL,

    -- Delivery Address
                        delivery_street VARCHAR(200),
                        delivery_city VARCHAR(50) NOT NULL,
                        delivery_state VARCHAR(50),
                        delivery_postal_code VARCHAR(20),
                        delivery_country VARCHAR(50),
                        delivery_latitude DOUBLE,
                        delivery_longitude DOUBLE,

    -- Restaurant Address
                        restaurant_street VARCHAR(200),
                        restaurant_city VARCHAR(50) NOT NULL,
                        restaurant_state VARCHAR(50),
                        restaurant_postal_code VARCHAR(20),
                        restaurant_country VARCHAR(50),
                        restaurant_latitude DOUBLE,
                        restaurant_longitude DOUBLE,

                        total_amount DECIMAL(10,2),
                        delivery_charge DECIMAL(10,2),
                        tax_amount DECIMAL(10,2),
                        grand_total DECIMAL(10,2),
                        delivery_partner_id BIGINT,
                        estimated_delivery_minutes INT,
                        actual_delivery_minutes INT,
                        special_instructions VARCHAR(500),
                        cancellation_reason VARCHAR(500),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP,
                        assigned_at TIMESTAMP,
                        picked_at TIMESTAMP,
                        delivered_at TIMESTAMP,
                        cancelled_at TIMESTAMP
);

-- Create order_items table
CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             item_id VARCHAR(50) NOT NULL,
                             item_name VARCHAR(200) NOT NULL,
                             quantity INT NOT NULL,
                             unit_price DECIMAL(10,2) NOT NULL,
                             total_price DECIMAL(10,2),
                             FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_restaurant_id ON orders(restaurant_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_delivery_city ON orders(delivery_city);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_delivery_partners_status ON delivery_partners(status);
CREATE INDEX idx_delivery_partners_city ON delivery_partners(current_city);
CREATE INDEX idx_delivery_partners_active ON delivery_partners(is_active);