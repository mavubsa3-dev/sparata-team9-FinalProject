INSERT INTO categories (id, name, created_at, updated_at) VALUES (1, '상의', NOW(), NOW());
INSERT INTO categories (id, name, created_at, updated_at) VALUES (2, '하의', NOW(), NOW());
INSERT INTO categories (id, name, created_at, updated_at) VALUES (3, '신발', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (1, '반팔 티셔츠', '시원한 여름용 반팔티입니다.', 'https://example.com/tshirt.jpg', 15000, 100, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (1, '긴팔 셔츠', '가을용 긴팔 셔츠입니다.', 'https://example.com/shirt.jpg', 29000, 50, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (2, '청바지', '데일리로 입기 좋은 청바지입니다.', 'https://example.com/jeans.jpg', 45000, 30, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (3, '운동화', '가볍고 편한 운동화입니다.', 'https://example.com/shoes.jpg', 68000, 0, 'SOLD_OUT', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (2, '단종된 바지', '더 이상 판매하지 않는 상품입니다.', 'https://example.com/old-pants.jpg', 20000, 0, 'HIDDEN', NOW(), NOW());

INSERT INTO users (id, email, password, name, phone_number, role, created_at, updated_at)
VALUES (1, 'test@example.com', 'encodedpassword', '테스트유저', '010-1234-5678', 'USER', NOW(), NOW());

INSERT INTO carts (id, user_id, created_at, updated_at)
VALUES (1, 1, NOW(), NOW());

INSERT INTO cart_items (cart_id, user_id, product_id, quantity, created_at, updated_at)
VALUES (1, 1, 1, 2, NOW(), NOW());

INSERT INTO cart_items (cart_id, user_id, product_id, quantity, created_at, updated_at)
VALUES (1, 1, 3, 1, NOW(), NOW());