INSERT INTO categories (name, created_at, updated_at) VALUES ('상의', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (1, '반팔 티셔츠', '시원한 여름용 반팔티입니다.', 'https://example.com/tshirt.jpg', 15000, 100, 'ON_SALE', NOW(), NOW());