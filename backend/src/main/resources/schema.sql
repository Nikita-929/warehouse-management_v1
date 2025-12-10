-- Warehouse Management System Database Schema
-- Version: 2.1

-- Create database (uncomment if needed)
-- CREATE DATABASE warehouse_db;
-- USE warehouse_db;

-- Products table - Main inventory table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    packets DECIMAL(10,2) DEFAULT 0.00,
    qty_per_packet DECIMAL(10,2) DEFAULT 0.00,
    quantity DECIMAL(10,2) NOT NULL,
    unit VARCHAR(100) NOT NULL,
    batch_no VARCHAR(255),
    grn_no VARCHAR(255),
    sales_invoice_no VARCHAR(255),
    material_type ENUM('RM', 'PM', 'FM') NOT NULL,
    source VARCHAR(255) NOT NULL,
    date_added DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_product_code (product_code),
    INDEX idx_material_type (material_type),
    INDEX idx_created_at (created_at)
);

-- Product names table - For autocomplete suggestions
CREATE TABLE IF NOT EXISTS product_names (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    INDEX idx_name (name)
);

-- Suppliers table - For supplier information
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100),
    INDEX idx_name (name)
);

-- Sample data for testing
INSERT IGNORE INTO product_names (name) VALUES
('Steel Rod'),
('Aluminum Sheet'),
('Plastic Container'),
('Cardboard Box'),
('Finished Widget'),
('Raw Material A'),
('Packing Tape'),
('Bubble Wrap');

INSERT IGNORE INTO suppliers (name, type) VALUES
('ABC Steel Corp', 'Raw Material Supplier'),
('XYZ Packaging Ltd', 'Packing Material Supplier'),
('Manufacturing Floor A', 'Internal Production'),
('Client ABC', 'External Client'),
('Supplier XYZ', 'General Supplier');

-- Sample products for testing
INSERT IGNORE INTO products (
    product_code, product_name, packets, qty_per_packet, quantity, 
    unit, batch_no, grn_no, sales_invoice_no, material_type, source, date_added
) VALUES
('P001', 'Steel Rod', 10.00, 5.00, 50.00, 'kg', 'B001', 'GRN001', 'INV001', 'RM', 'ABC Steel Corp', '2024-01-01 10:00:00'),
('P002', 'Aluminum Sheet', 5.00, 2.00, 10.00, 'pieces', 'B002', 'GRN002', 'INV002', 'RM', 'ABC Steel Corp', '2024-01-02 11:00:00'),
('P003', 'Cardboard Box', 20.00, 1.00, 20.00, 'pieces', 'B003', 'GRN003', 'INV003', 'PM', 'XYZ Packaging Ltd', '2024-01-03 12:00:00'),
('P004', 'Finished Widget', 15.00, 3.00, 45.00, 'pieces', 'B004', 'GRN004', 'INV004', 'FM', 'Manufacturing Floor A', '2024-01-04 13:00:00');

-- Transactions table for tracking IN/OUT movements
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    barcode VARCHAR(255),
    product_code VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    unit VARCHAR(100) NOT NULL,
    batch_no VARCHAR(255),
    grn_no VARCHAR(255),
    material_type ENUM('RM', 'PM', 'FM') NOT NULL,
    type ENUM('IN', 'OUT') NOT NULL,
    party VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_transactions_type (type),
    INDEX idx_transactions_material_type (material_type),
    INDEX idx_transactions_created_at (created_at),
    INDEX idx_transactions_party (party),
    INDEX idx_transactions_product_code (product_code)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_products_search ON products(product_name, product_code, source);
CREATE INDEX IF NOT EXISTS idx_products_date ON products(date_added);
CREATE INDEX IF NOT EXISTS idx_products_batch ON products(batch_no);
