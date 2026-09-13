-- ============================================================
-- Seed data for local development
-- ============================================================
USE ecommerce;

-- Users
INSERT INTO `user` (user_id, email, password, nick_name, gender, balance) VALUES
('U1001', 'alice@example.com', '$2a$10$demo', 'Alice', 'F', 500.00),
('U1002', 'bob@example.com',   '$2a$10$demo', 'Bob',   'M', 1200.50),
('U1003', 'carol@example.com', '$2a$10$demo', 'Carol', 'F', 80.00);

-- Addresses
INSERT INTO address (address_id, user_id, name, phone, detailed_address, state, completed_address) VALUES
('A2001', 'U1001', 'Alice Zhang', '13800001111', 'Room 301, Building 5, Sunshine Community', '1', 'Beijing, Haidian, Sunshine Community, Room 301'),
('A2002', 'U1002', 'Bob Li',      '13900002222', 'Apt 12B, Tower 2, Lakeview Garden',     '1', 'Shanghai, Pudong, Lakeview Garden, Apt 12B'),
('A2003', 'U1003', 'Carol Wang',  '13700003333', 'No.88, Technology Road',                  '0', 'Shenzhen, Nanshan, Technology Road No.88');

-- Goods
INSERT INTO goods (goods_id, goods_name, original_price, discount_price, master_img, intro, address, postage, inventory, sale_volume) VALUES
('G3001', 'Wireless Bluetooth Headphones', 299.00, 199.00, '/img/g3001.jpg', 'Noise-cancelling, 30h battery', 'Shenzhen',  10, 500, 1280),
('G3002', 'Mechanical Keyboard 87-key',    459.00, 359.00, '/img/g3002.jpg', 'Hot-swappable, RGB backlight',  'Dongguan',  0, 200,  560),
('G3003', 'USB-C Fast Charger 65W',         99.00,  69.00, '/img/g3003.jpg', 'GaN tech, compact size',        'Shenzhen',   8, 1000, 3400),
('G3004', 'Ergonomic Mouse',               189.00, 129.00, '/img/g3004.jpg', 'Vertical design, wrist-friendly','Hangzhou',  10,  80,  210);

-- Orders (some pending, some completed)
INSERT INTO orders (order_id, user_id, goods_id, purchase_num, address_id, order_state, total_money) VALUES
('O4001', 'U1001', 'G3001', 1, 'A2001', 'COMPLETED',   199),
('O4002', 'U1002', 'G3003', 2, 'A2002', 'PAID',        138),
('O4003', 'U1001', 'G3002', 1, 'A2001', 'PENDING_PAY', 359),
('O4004', 'U1003', 'G3004', 1, 'A2003', 'SHIPPED',     129);
