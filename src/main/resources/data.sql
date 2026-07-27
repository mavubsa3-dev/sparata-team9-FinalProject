INSERT INTO categories (id, name, created_at, updated_at) VALUES (1, '상의', NOW(), NOW());
INSERT INTO categories (id, name, created_at, updated_at) VALUES (2, '하의', NOW(), NOW());
INSERT INTO categories (id, name, created_at, updated_at) VALUES (3, '신발', NOW(), NOW());
INSERT INTO categories (id, name, created_at, updated_at) VALUES (4, '아우터', NOW(), NOW());
INSERT INTO categories (id, name, created_at, updated_at) VALUES (5, '가방', NOW(), NOW());

-- 상품 추가 (기존 5개 + 15개 = 총 20개)

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

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (1, '흰색 반팔티', '베이직한 흰색 반팔티입니다.', 'https://example.com/white-tshirt.jpg', 12000, 200, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (1, '스트라이프 반팔티', '줄무늬 패턴의 반팔티입니다.', 'https://example.com/stripe-tshirt.jpg', 18000, 80, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (1, '반팔 니트', '여름용 얇은 니트입니다.', 'https://example.com/knit.jpg', 35000, 45, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (1, '린넨 셔츠', '시원한 린넨 소재 셔츠입니다.', 'https://example.com/linen.jpg', 42000, 0, 'SOLD_OUT', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (2, '슬랙스', '깔끔한 슬랙스입니다.', 'https://example.com/slacks.jpg', 55000, 60, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (2, '반바지', '편안한 여름 반바지입니다.', 'https://example.com/shorts.jpg', 25000, 90, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (2, '조거 팬츠', '활동적인 조거 팬츠입니다.', 'https://example.com/jogger.jpg', 38000, 5, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (2, '와이드 팬츠', '트렌디한 와이드 팬츠입니다.', 'https://example.com/wide.jpg', 48000, 0, 'SOLD_OUT', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (3, '슬리퍼', '편안한 여름 슬리퍼입니다.', 'https://example.com/slipper.jpg', 15000, 150, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (3, '샌들', '가벼운 여름 샌들입니다.', 'https://example.com/sandal.jpg', 32000, 70, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (3, '부츠', '가을 겨울 부츠입니다.', 'https://example.com/boots.jpg', 95000, 20, 'HIDDEN', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (4, '가디건', '얇은 봄 가디건입니다.', 'https://example.com/cardigan.jpg', 45000, 35, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (4, '자켓', '캐주얼 자켓입니다.', 'https://example.com/jacket.jpg', 89000, 25, 'ON_SALE', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (4, '코트', '겨울 롱코트입니다.', 'https://example.com/coat.jpg', 150000, 10, 'HIDDEN', NOW(), NOW());

INSERT INTO products (category_id, name, description, thumbnail_url, price, stock, status, created_at, updated_at)
VALUES (5, '크로스백', '가벼운 크로스백입니다.', 'https://example.com/crossbag.jpg', 55000, 40, 'ON_SALE', NOW(), NOW());

INSERT INTO users (id, email, password, name, phone_number, role, created_at, updated_at)
VALUES (1, 'test@example.com', 'encodedpassword', '테스트유저', '010-1234-5678', 'USER', NOW(), NOW());

INSERT INTO carts (id, user_id, created_at, updated_at)
VALUES (1, 1, NOW(), NOW());

INSERT INTO cart_items (cart_id, user_id, product_id, quantity, created_at, updated_at)
VALUES (1, 1, 1, 2, NOW(), NOW());

INSERT INTO cart_items (cart_id, user_id, product_id, quantity, created_at, updated_at)
VALUES (1, 1, 3, 1, NOW(), NOW());
