INSERT INTO users (id, created_at, updated_at, email, name, password, phone_number, role) VALUES
                                                                                              (1, NOW(), NOW(), 'admin@test.com', '최고관리자', 'encrypted_pwd_123', '010-1111-1111', 'admin'),
                                                                                              (2, NOW(), NOW(), 'user@test.com', '일반고객', 'encrypted_pwd_456', '010-2222-2222', 'user'),
                                                                                              (3, NOW(), NOW(), 'test3@test.com', '김테스트', 'encrypted_pwd', '010-3333-3333', 'user'),
                                                                                              (4, NOW(), NOW(), 'test4@test.com', '이테스트', 'encrypted_pwd', '010-4444-4444', 'user'),
                                                                                              (5, NOW(), NOW(), 'test5@test.com', '박테스트', 'encrypted_pwd', '010-5555-5555', 'user'),
                                                                                              (6, NOW(), NOW(), 'test6@test.com', '최테스트', 'encrypted_pwd', '010-6666-6666', 'user'),
                                                                                              (7, NOW(), NOW(), 'test7@test.com', '정테스트', 'encrypted_pwd', '010-7777-7777', 'user');

-- 2. 카테고리 (categories) 총 7개 (모두 최고관리자가 생성)
INSERT INTO categories (id, created_at, updated_at, name, admin_id) VALUES
                                                                        (1, NOW(), NOW(), '전자기기', 1),
                                                                        (2, NOW(), NOW(), '의류', 1),
                                                                        (3, NOW(), NOW(), '식품', 1),
                                                                        (4, NOW(), NOW(), '가전', 1),
                                                                        (5, NOW(), NOW(), '도서', 1),
                                                                        (6, NOW(), NOW(), '스포츠', 1),
                                                                        (7, NOW(), NOW(), '뷰티', 1);

-- 3. 상품 (products) 총 8개 (다양한 카테고리 및 상태)
INSERT INTO products (id, created_at, updated_at, category_id, name, description, thumbnail_url, price, stock, status) VALUES
                                                                                                                           (1, NOW(), NOW(), 1, '아이폰 17 Pro', '최신형 스마트폰입니다.', 'url/iphone17.jpg', 1500000, 50, 'ON_SALE'),
                                                                                                                           (2, NOW(), NOW(), 1, 'LG Gram 노트북', '가볍고 성능 좋은 노트북입니다.', 'url/gram.jpg', 2000000, 30, 'ON_SALE'),
                                                                                                                           (3, NOW(), NOW(), 2, '검은색 반팔 티셔츠', '여름용 무지 티셔츠입니다.', 'url/tshirt.jpg', 15000, 100, 'ON_SALE'),
                                                                                                                           (4, NOW(), NOW(), 3, '유기농 사과 1kg', '맛있는 사과', 'url/apple.jpg', 12000, 100, 'ON_SALE'),
                                                                                                                           (5, NOW(), NOW(), 4, '로봇 청소기', '구형 모델 단종', 'url/robot.jpg', 350000, 0, 'HIDDEN'),
                                                                                                                           (6, NOW(), NOW(), 5, '스프링 부트 교재', '베스트셀러', 'url/book.jpg', 32000, 0, 'SOLD_OUT'),
                                                                                                                           (7, NOW(), NOW(), 6, '요가매트', '친환경 소재', 'url/yoga.jpg', 25000, 50, 'ON_SALE'),
                                                                                                                           (8, NOW(), NOW(), 7, '수분 크림', '인기 화장품', 'url/cream.jpg', 18000, 200, 'ON_SALE');

-- 4. 배송지 (addresses) 총 6개 (유저 2~7번)
INSERT INTO addresses (id, created_at, updated_at, user_id, alias, name, phone_number, zip_code, address1, address2) VALUES
                                                                                                                         (1, NOW(), NOW(), 2, '우리집', '일반고객', '010-2222-2222', '12345', '서울시 강남구 테헤란로', '101동 202호'),
                                                                                                                         (2, NOW(), NOW(), 3, '회사', '김테스트', '010-3333-3333', '06234', '서울시 강남구 역삼동', '위워크 3층'),
                                                                                                                         (3, NOW(), NOW(), 4, '본가', '이테스트', '010-4444-4444', '48021', '부산시 해운대구 우동', '자이아파트 101동'),
                                                                                                                         (4, NOW(), NOW(), 5, '우리집', '박테스트', '010-5555-5555', '13524', '경기도 성남시 분당구', '판교동 123-4'),
                                                                                                                         (5, NOW(), NOW(), 6, '친구집', '친구이름', '010-9999-9999', '01234', '서울시 송파구 잠실동', '엘스아파트'),
                                                                                                                         (6, NOW(), NOW(), 7, '기숙사', '정테스트', '010-7777-7777', '54321', '대전광역시 유성구 대학로', '학생생활관 1관');
