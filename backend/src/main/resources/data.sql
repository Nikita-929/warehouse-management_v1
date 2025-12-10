-- Sample data for Warehouse Management System
-- This file will be executed after schema.sql

-- Insert additional product names for better autocomplete
INSERT IGNORE INTO product_names (name) VALUES
('Copper Wire'),
('Rubber Gasket'),
('Glass Panel'),
('Wooden Pallet'),
('Metal Bracket'),
('Fiber Cable'),
('Ceramic Insulator'),
('Composite Material'),
('Thermal Paste'),
('Insulation Foam');

-- Insert additional suppliers
INSERT IGNORE INTO suppliers (name, type) VALUES
('Metro Steel Industries', 'Raw Material Supplier'),
('Global Packaging Solutions', 'Packing Material Supplier'),
('Precision Components Inc', 'Component Supplier'),
('Quality Materials Co', 'Raw Material Supplier'),
('Packaging World', 'Packing Material Supplier'),
('Production Line B', 'Internal Production'),
('Production Line C', 'Internal Production'),
('Client DEF', 'External Client'),
('Client GHI', 'External Client'),
('Supplier 123', 'General Supplier');

-- Insert more sample products
INSERT IGNORE INTO products (
    product_code, product_name, packets, qty_per_packet, quantity, 
    unit, batch_no, grn_no, sales_invoice_no, material_type, source, date_added
) VALUES
('P005', 'Copper Wire', 8.00, 10.00, 80.00, 'meters', 'B005', 'GRN005', 'INV005', 'RM', 'Metro Steel Industries', '2024-01-05 09:00:00'),
('P006', 'Rubber Gasket', 50.00, 1.00, 50.00, 'pieces', 'B006', 'GRN006', 'INV006', 'RM', 'Precision Components Inc', '2024-01-06 10:00:00'),
('P007', 'Glass Panel', 3.00, 2.00, 6.00, 'pieces', 'B007', 'GRN007', 'INV007', 'RM', 'Quality Materials Co', '2024-01-07 11:00:00'),
('P008', 'Wooden Pallet', 12.00, 1.00, 12.00, 'pieces', 'B008', 'GRN008', 'INV008', 'PM', 'Global Packaging Solutions', '2024-01-08 12:00:00'),
('P009', 'Metal Bracket', 25.00, 4.00, 100.00, 'pieces', 'B009', 'GRN009', 'INV009', 'RM', 'Metro Steel Industries', '2024-01-09 13:00:00'),
('P010', 'Fiber Cable', 6.00, 15.00, 90.00, 'meters', 'B010', 'GRN010', 'INV010', 'RM', 'Supplier 123', '2024-01-10 14:00:00'),
('P011', 'Ceramic Insulator', 40.00, 2.00, 80.00, 'pieces', 'B011', 'GRN011', 'INV011', 'RM', 'Precision Components Inc', '2024-01-11 15:00:00'),
('P012', 'Composite Material', 7.00, 8.00, 56.00, 'kg', 'B012', 'GRN012', 'INV012', 'RM', 'Quality Materials Co', '2024-01-12 16:00:00'),
('P013', 'Thermal Paste', 30.00, 1.00, 30.00, 'tubes', 'B013', 'GRN013', 'INV013', 'PM', 'Packaging World', '2024-01-13 17:00:00'),
('P014', 'Insulation Foam', 4.00, 5.00, 20.00, 'sheets', 'B014', 'GRN014', 'INV014', 'PM', 'Global Packaging Solutions', '2024-01-14 18:00:00'),
('P015', 'Assembly Unit A', 10.00, 3.00, 30.00, 'pieces', 'B015', 'GRN015', 'INV015', 'FM', 'Production Line B', '2024-01-15 19:00:00'),
('P016', 'Assembly Unit B', 8.00, 2.00, 16.00, 'pieces', 'B016', 'GRN016', 'INV016', 'FM', 'Production Line C', '2024-01-16 20:00:00'),
('P017', 'Final Product X', 5.00, 1.00, 5.00, 'units', 'B017', 'GRN017', 'INV017', 'FM', 'Production Line A', '2024-01-17 21:00:00'),
('P018', 'Final Product Y', 3.00, 1.00, 3.00, 'units', 'B018', 'GRN018', 'INV018', 'FM', 'Production Line B', '2024-01-18 22:00:00');

-- Insert sample transaction data
INSERT IGNORE INTO transactions (
    barcode, product_code, product_name, quantity, unit, batch_no, grn_no, 
    material_type, type, party, created_at
) VALUES
('123456789', 'P001', 'Steel Rod', 50.00, 'kg', 'B001', 'GRN001', 'RM', 'IN', 'ABC Steel Corp', '2024-01-01 10:00:00'),
('987654321', 'P002', 'Aluminum Sheet', 10.00, 'pieces', 'B002', 'GRN002', 'RM', 'IN', 'ABC Steel Corp', '2024-01-02 11:00:00'),
('456789123', 'P003', 'Cardboard Box', 20.00, 'pieces', 'B003', 'GRN003', 'PM', 'IN', 'XYZ Packaging Ltd', '2024-01-03 12:00:00'),
('789123456', 'P001', 'Steel Rod', 25.00, 'kg', 'B001', 'GRN001', 'RM', 'OUT', 'Production Floor A', '2024-01-04 13:00:00'),
('321654987', 'P004', 'Finished Widget', 45.00, 'pieces', 'B004', 'GRN004', 'FM', 'IN', 'Manufacturing Floor A', '2024-01-05 14:00:00'),
('654987321', 'P002', 'Aluminum Sheet', 5.00, 'pieces', 'B002', 'GRN002', 'RM', 'OUT', 'Client ABC', '2024-01-06 15:00:00'),
('147258369', 'P005', 'Copper Wire', 80.00, 'meters', 'B005', 'GRN005', 'RM', 'IN', 'Metro Steel Industries', '2024-01-07 16:00:00'),
('369258147', 'P006', 'Rubber Gasket', 50.00, 'pieces', 'B006', 'GRN006', 'RM', 'IN', 'Precision Components Inc', '2024-01-08 17:00:00'),
('258147369', 'P007', 'Glass Panel', 6.00, 'pieces', 'B007', 'GRN007', 'RM', 'IN', 'Quality Materials Co', '2024-01-09 18:00:00'),
('741852963', 'P008', 'Wooden Pallet', 12.00, 'pieces', 'B008', 'GRN008', 'PM', 'IN', 'Global Packaging Solutions', '2024-01-10 19:00:00'),
('963852741', 'P009', 'Metal Bracket', 100.00, 'pieces', 'B009', 'GRN009', 'RM', 'IN', 'Metro Steel Industries', '2024-01-11 20:00:00'),
('852963741', 'P010', 'Fiber Cable', 90.00, 'meters', 'B010', 'GRN010', 'RM', 'IN', 'Supplier 123', '2024-01-12 21:00:00'),
('741963852', 'P011', 'Ceramic Insulator', 80.00, 'pieces', 'B011', 'GRN011', 'RM', 'IN', 'Precision Components Inc', '2024-01-13 22:00:00'),
('963741852', 'P012', 'Composite Material', 56.00, 'kg', 'B012', 'GRN012', 'RM', 'IN', 'Quality Materials Co', '2024-01-14 23:00:00'),
('852741963', 'P013', 'Thermal Paste', 30.00, 'tubes', 'B013', 'GRN013', 'PM', 'IN', 'Packaging World', '2024-01-15 00:00:00'),
('147963852', 'P014', 'Insulation Foam', 20.00, 'sheets', 'B014', 'GRN014', 'PM', 'IN', 'Global Packaging Solutions', '2024-01-16 01:00:00'),
('741852963', 'P015', 'Assembly Unit A', 30.00, 'pieces', 'B015', 'GRN015', 'FM', 'IN', 'Production Line B', '2024-01-17 02:00:00'),
('852963741', 'P016', 'Assembly Unit B', 16.00, 'pieces', 'B016', 'GRN016', 'FM', 'IN', 'Production Line C', '2024-01-18 03:00:00'),
('963741852', 'P017', 'Final Product X', 5.00, 'units', 'B017', 'GRN017', 'FM', 'IN', 'Production Line A', '2024-01-19 04:00:00'),
('741852963', 'P018', 'Final Product Y', 3.00, 'units', 'B018', 'GRN018', 'FM', 'IN', 'Production Line B', '2024-01-20 05:00:00'),
('123456789', 'P001', 'Steel Rod', 15.00, 'kg', 'B001', 'GRN001', 'RM', 'OUT', 'Client DEF', '2024-01-21 06:00:00'),
('987654321', 'P002', 'Aluminum Sheet', 8.00, 'pieces', 'B002', 'GRN002', 'RM', 'OUT', 'Production Floor B', '2024-01-22 07:00:00'),
('456789123', 'P003', 'Cardboard Box', 12.00, 'pieces', 'B003', 'GRN003', 'PM', 'OUT', 'Client GHI', '2024-01-23 08:00:00'),
('789123456', 'P004', 'Finished Widget', 20.00, 'pieces', 'B004', 'GRN004', 'FM', 'OUT', 'Client ABC', '2024-01-24 09:00:00');