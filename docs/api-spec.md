# API 명세
이 폴더는 9신사 프로젝트의 API 명세를 도메인별로 나눈 문서입니다.

## 인증/인가

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/auth/signup` | 회원가입 | 불필요 |
| POST | `/api/auth/login` | 로그인 (JWT/Refresh 토큰 발급) | 불필요 |
| POST | `/api/auth/reissue` | 토큰 재발급 | 불필요 (리프레시 토큰 필요) |
| POST | `/api/auth/logout` | 로그아웃 | 필요 |

### POST /api/auth/signup - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| email | String | 필수 | 이메일 (이메일 형식 검증) |
| password | String | 필수 | 비밀번호 (영문·숫자·특수문자 포함 8~15자) |
| name | String | 필수 | 이름 |
| phoneNumber | String | 필수 | 전화번호 (예: 010-1234-5678) |

### POST /api/auth/login - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| email | String | 필수 | 이메일 |
| password | String | 필수 | 비밀번호 |

### POST /api/auth/reissue - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| refreshToken | String | 필수 | 재발급에 사용할 리프레시 토큰 |

POST /api/auth/logout은 Request Body 없이 인증 토큰만으로 처리됩니다.

## 사용자

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/user/me` | 사용자 정보 조회 | 필요 |
| PATCH | `/api/user/me` | 사용자 정보 수정 | 필요 |
| GET | `/api/addresses` | 배송지 목록 조회 | 필요 |
| POST | `/api/addresses` | 배송지 추가 | 필요 |
| PATCH | `/api/addresses/{addressId}` | 배송지 수정 | 필요 |
| DELETE | `/api/addresses/{addressId}` | 배송지 삭제 | 필요 |

### PATCH /api/user/me - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| name | String | 선택 | 변경할 이름 |
| password | String | 선택 | 변경할 비밀번호 (영문·숫자·특수문자 포함 8~15자) |
| phoneNumber | String | 선택 | 변경할 휴대폰 번호 |

### POST /api/addresses - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| alias | String | 필수 | 배송지 별칭 |
| name | String | 필수 | 수령인 이름 |
| phoneNumber | String | 필수 | 수령인 휴대폰 번호 |
| zipCode | String | 필수 | 우편번호 (5자리 숫자) |
| basicAddress | String | 필수 | 기본 주소 |
| detailAddress | String | 선택 | 상세 주소 |

### PATCH /api/addresses/{addressId} - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| alias | String | 선택 | 배송지 별칭 |
| name | String | 선택 | 수령인 이름 |
| phoneNumber | String | 선택 | 수령인 휴대폰 번호 |
| zipCode | String | 선택 | 우편번호 (5자리 숫자) |
| basicAddress | String | 선택 | 기본 주소 |
| detailAddress | String | 선택 | 상세 주소 |
| isDefault | boolean | 필수 | 기본 배송지 여부 |

## 상품

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/products` | 상품 목록 조회 (categoryId 필터) | 불필요 |
| GET | `/api/products/{productId}` | 상품 상세 조회 | 불필요 |
| POST | `/api/admin/products/images` | 상품 이미지 업로드 | 관리자 |

POST /api/admin/products/images는 JSON Request Body가 아닌 `multipart/form-data`로 파일(`file`, MultipartFile)을 전송합니다.

## 카테고리

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/categories` | 카테고리 목록 조회 | 불필요 |

## 장바구니

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/carts` | 장바구니 조회 | 필요 |
| GET | `/api/carts/count` | 장바구니 아이템 개수 조회 | 필요 |
| POST | `/api/carts/items` | 장바구니 상품 추가 | 필요 |
| PATCH | `/api/carts/items/{cartItemId}` | 장바구니 수량 수정 | 필요 |
| DELETE | `/api/carts/items/{cartItemId}` | 장바구니 상품 개별 삭제 | 필요 |
| DELETE | `/api/carts/clear` | 장바구니 전체 비우기 | 필요 |

### POST /api/carts/items - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| productId | Long | 필수 | 상품 ID |
| quantity | Integer | 필수 | 담을 수량 (1 이상) |

### PATCH /api/carts/items/{cartItemId} - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| quantity | Integer | 필수 | 변경할 수량 (1 이상) |

## 관리자

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/admin/categories` | 카테고리 등록 | 관리자 |
| GET | `/api/admin/categories` | 카테고리 목록 조회 (관리자) | 관리자 |
| PATCH | `/api/admin/categories/{categoryId}` | 카테고리 수정 | 관리자 |
| DELETE | `/api/admin/categories/{categoryId}` | 카테고리 삭제 | 관리자 |
| POST | `/api/admin/products` | 상품 등록 | 관리자 |
| GET | `/api/admin/products` | 상품 목록 조회 (검색/필터/페이징) | 관리자 |
| PATCH | `/api/admin/products/{productId}` | 상품 수정 | 관리자 |
| DELETE | `/api/admin/products/{productId}` | 상품 삭제 | 관리자 |
| GET | `/api/admin/payments` | 결제 목록 조회 | 관리자 |
| GET | `/api/admin/payments/{paymentId}` | 결제 단건 조회 | 관리자 |

### POST /api/admin/categories - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| name | String | 필수 | 카테고리 이름 |

### PATCH /api/admin/categories/{categoryId} - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| name | String | 필수 | 변경할 카테고리 이름 |

### POST /api/admin/products - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| categoryId | Long | 필수 | 카테고리 ID |
| name | String | 필수 | 상품 이름 |
| description | String | 필수 | 상품 설명 |
| thumbnailUrl | String | 선택 | 썸네일 이미지 URL (URL 형식 검증) |
| price | Long | 필수 | 상품 가격 (100원 이상) |
| stock | Integer | 필수 | 재고 수량 (1개 이상) |

### PATCH /api/admin/products/{productId} - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| categoryId | Long | 선택 | 변경할 카테고리 ID |
| name | String | 선택 | 변경할 상품 이름 |
| description | String | 선택 | 변경할 상품 설명 |
| thumbnailUrl | String | 선택 | 변경할 썸네일 URL (URL 형식 검증) |
| price | Long | 선택 | 변경할 가격 (100원 이상) |
| stock | Integer | 선택 | 변경할 재고 수량 (0 이상) |
| status | String (`ON_SALE`, `SOLD_OUT`, `HIDDEN`) | 선택 | 상품 상태 |

## 주문

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/orders` | 주문 생성 | 필요 |
| GET | `/api/orders` | 주문 목록 조회 | 필요 |
| GET | `/api/orders/{orderId}` | 주문 단건 상세 조회 | 필요 |
| PATCH | `/api/orders/{orderId}/cancel` | 결제 전 주문 취소 | 필요 |
| PATCH | `/api/orders/{orderId}/address` | 주문 배송지 지정 | 필요 |

### POST /api/orders - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| cartItemIds | List\<Long\> | 필수 | 주문할 장바구니 아이템 ID 목록 (빈 배열 불가) |

### PATCH /api/orders/{orderId}/address - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| addressId | Long | 필수 | 주문에 지정할 배송지 ID |

### 참고: 주문 자동취소 스케줄러

주문 생성 후 1시간 동안 결제되지 않은(ORDERED 상태) 주문은 5초 주기로 실행되는 백그라운드 스케줄러에 의해 자동으로 취소됩니다. 별도의 API 호출 없이 서버 내부에서 자동으로 처리됩니다.

## 결제

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/payments` | 결제 생성 | 필요 |
| PATCH | `/api/payments/{paymentId}/confirm` | 결제 승인 확정 | 필요 |
| GET | `/api/payments` | 결제 목록 조회 | 필요 |
| GET | `/api/payments/{paymentId}` | 결제 단건 조회 | 필요 |
| PATCH | `/api/payments/{paymentId}/cancel` | 결제 취소 | 필요 |

### POST /api/payments - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| orderId | Long | 필수 | 결제할 주문 ID |
| addressId | Long | 필수 | 배송지 ID |

### PATCH /api/payments/{paymentId}/confirm - Request Body

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| portonePaymentId | String | 필수 | PortOne 결제 ID |

PATCH /api/payments/{paymentId}/cancel은 Request Body 없이 경로 변수(paymentId)만으로 처리됩니다.

관리자용 결제 목록/단건 조회는 위 "관리자" 섹션의 `/api/admin/payments` 참고.

## PortOne 연동

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/config/portone` | 프론트 결제 설정값(storeId, channelKey) 제공 | 불필요 |
| POST | `/api/webhooks/portone` | PortOne 결제 웹훅 수신 | 불필요 (웹훅 서명 검증) |

### POST /api/webhooks/portone - Request Body

PortOne이 전송하는 원문 문자열(raw body)을 그대로 받아 서버 내부에서 파싱합니다. 요청 헤더로 `webhook-id`, `webhook-timestamp`, `webhook-signature`가 필요합니다.

| 필드명 | 타입 | 필수여부 | 설명 |
|---|---|---|---|
| type | String | PortOne 전송값 | 이벤트 타입 (예: `Transaction.Paid`) |
| timestamp | String | PortOne 전송값 | 이벤트 발생 시각 |
| data.paymentId | String | PortOne 전송값 | PortOne 결제 ID |

PortOne 결제 생성/조회/취소는 별도의 클라이언트용 엔드포인트가 아니라, 위 "결제" 섹션의 `/api/payments` 요청 처리 중 서버가 내부적으로 PortOne API를 호출하는 방식으로 연동됩니다.

## 정산

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/admin/settlements` | 정산 목록 조회 (전체 날짜, 최신순) | 필요 (관리자) |
| GET | `/api/admin/settlements/{settlementDate}` | 정산 단건 조회 (날짜별) | 필요 (관리자) |
| POST | `/api/admin/settlements/{settlementDate}/aggregate` | 특정 날짜 정산 수동 재집계 | 필요 (관리자) |

### GET, POST /api/admin/settlements/{settlementDate}(...)

- `settlementDate`는 경로 변수, ISO 날짜 형식 (`yyyy-MM-dd`, 예: `2026-08-20`)
- `POST .../aggregate`는 Request Body 없음. 이미 집계된 날짜를 다시 호출하면 기존 데이터를 갱신(upsert)합니다.

### 참고: 자동 정산 스케줄러

매일 00:00(KST)에 전날 하루치 매출을 자동으로 집계하여 저장합니다 (`SettlementScheduler`). 위 `POST .../aggregate` API는 이 배치를 수동으로 즉시 재실행하고 싶을 때 (자정을 기다리지 않고 테스트하거나,특정 날짜 데이터를 재집계할 때) 사용합니다.


## 알림

결제 완료 시 이메일 알림 발송은 REST API가 아니라, 결제 완료 Kafka 이벤트를 구독하는 리스너(`PaymentEventListener`)가 트리거합니다. 별도로 호출 가능한 엔드포인트는 없습니다.

## 인기 상품 랭킹

| Method | URL | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/products/ranking` | 일일 인기 상품 랭킹 조회 (count 파라미터) | 불필요 |
| GET | `/api/products/ranking/week` | 주간 인기 상품 랭킹 조회 (count 파라미터) | 불필요 |
| GET | `/api/products/ranking/{productId}` | 일일 랭킹 내 특정 상품 정보 조회 | 불필요 |
| GET | `/api/products/ranking/week/{productId}` | 주간 랭킹 내 특정 상품 정보 조회 | 불필요 |
