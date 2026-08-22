/**
 * 목데이터 모음.
 * 실제 백엔드 연동 전까지 화면 레이아웃 확인용으로 사용합니다.
 * 필드명은 docs/api-spec.md 의 응답 DTO 구조를 그대로 따릅니다.
 * 주문/결제 도메인은 아직 백엔드 로직이 확정되지 않아 데이터 형태가 바뀔 수 있습니다.
 */

const THUMB_PALETTE = [
  "#111114",
  "#2b6bff",
  "#ff3d5a",
  "#1a9e5c",
  "#e08a00",
  "#6b4fbb",
  "#0e8f8f",
];

function thumbColor(seed) {
  let hash = 0;
  for (let i = 0; i < seed.length; i++) {
    hash = (hash * 31 + seed.charCodeAt(i)) % THUMB_PALETTE.length;
  }
  return THUMB_PALETTE[Math.abs(hash)];
}

function buildThumbPlaceholder(name) {
  const el = document.createElement("div");
  el.className = "thumb-placeholder";
  el.style.cssText = `width:100%;height:100%;display:flex;align-items:center;justify-content:center;color:#fff;font-weight:800;font-size:13px;text-align:center;white-space:normal;padding:8px;background:${thumbColor(
    name
  )};`;
  el.textContent = name;
  return el;
}

/**
 * 상품 썸네일 엘리먼트를 만든다. thumbnailUrl이 있으면 이미지를,
 * 없거나 로드에 실패하면(더미 데이터의 가짜 URL 등) 색상 플레이스홀더로 대체한다.
 */
function renderThumb(name, thumbnailUrl) {
  if (thumbnailUrl) {
    const img = document.createElement("img");
    img.src = thumbnailUrl;
    img.alt = name;
    img.style.cssText = "width:100%;height:100%;object-fit:cover;display:block;";
    img.onerror = () => {
      if (img.isConnected) img.replaceWith(buildThumbPlaceholder(name));
    };
    return img;
  }
  return buildThumbPlaceholder(name);
}

const MOCK_CATEGORIES = [
  { id: 1, name: "아우터" },
  { id: 2, name: "상의" },
  { id: 3, name: "하의" },
  { id: 4, name: "신발" },
  { id: 5, name: "가방" },
  { id: 6, name: "액세서리" },
];

const MOCK_PRODUCTS = [
  { id: 101, categoryId: 1, categoryName: "아우터", name: "오버사이즈 울 코트", description: "겨울용 오버사이즈 울 코트입니다. 안감 기모 처리로 보온성이 뛰어납니다.", thumbnailUrl: "", price: 189000, stock: 12, status: "ON_SALE" },
  { id: 102, categoryId: 1, categoryName: "아우터", name: "경량 패딩 점퍼", description: "가볍고 따뜻한 경량 패딩 점퍼.", thumbnailUrl: "", price: 129000, stock: 0, status: "SOLD_OUT" },
  { id: 103, categoryId: 1, categoryName: "아우터", name: "데님 자켓", description: "베이직한 워싱 데님 자켓.", thumbnailUrl: "", price: 79000, stock: 30, status: "ON_SALE" },
  { id: 201, categoryId: 2, categoryName: "상의", name: "오버핏 코튼 티셔츠", description: "부드러운 코튼 소재의 오버핏 티셔츠.", thumbnailUrl: "", price: 29000, stock: 120, status: "ON_SALE" },
  { id: 202, categoryId: 2, categoryName: "상의", name: "스트라이프 니트", description: "가을에 어울리는 스트라이프 니트.", thumbnailUrl: "", price: 49000, stock: 45, status: "ON_SALE" },
  { id: 203, categoryId: 2, categoryName: "상의", name: "후드 스웨트셔츠", description: "기본에 충실한 후드 스웨트셔츠.", thumbnailUrl: "", price: 39000, stock: 60, status: "ON_SALE" },
  { id: 204, categoryId: 2, categoryName: "상의", name: "체크 셔츠", description: "캐주얼한 체크 패턴 셔츠.", thumbnailUrl: "", price: 45000, stock: 0, status: "SOLD_OUT" },
  { id: 301, categoryId: 3, categoryName: "하의", name: "와이드 데님 팬츠", description: "편안한 핏의 와이드 데님 팬츠.", thumbnailUrl: "", price: 59000, stock: 80, status: "ON_SALE" },
  { id: 302, categoryId: 3, categoryName: "하의", name: "슬랙스", description: "깔끔한 세미 와이드 슬랙스.", thumbnailUrl: "", price: 55000, stock: 40, status: "ON_SALE" },
  { id: 303, categoryId: 3, categoryName: "하의", name: "조거 팬츠", description: "활동성이 좋은 조거 팬츠.", thumbnailUrl: "", price: 42000, stock: 55, status: "ON_SALE" },
  { id: 401, categoryId: 4, categoryName: "신발", name: "레더 스니커즈", description: "미니멀한 디자인의 레더 스니커즈.", thumbnailUrl: "", price: 99000, stock: 25, status: "ON_SALE" },
  { id: 402, categoryId: 4, categoryName: "신발", name: "첼시 부츠", description: "클래식한 첼시 부츠.", thumbnailUrl: "", price: 139000, stock: 15, status: "ON_SALE" },
  { id: 501, categoryId: 5, categoryName: "가방", name: "크로스백", description: "실용적인 사이즈의 크로스백.", thumbnailUrl: "", price: 69000, stock: 20, status: "ON_SALE" },
  { id: 502, categoryId: 5, categoryName: "가방", name: "백팩", description: "노트북 수납이 가능한 백팩.", thumbnailUrl: "", price: 89000, stock: 18, status: "ON_SALE" },
  { id: 601, categoryId: 6, categoryName: "액세서리", name: "니트 비니", description: "따뜻한 니트 비니.", thumbnailUrl: "", price: 19000, stock: 100, status: "ON_SALE" },
  { id: 602, categoryId: 6, categoryName: "액세서리", name: "레더 벨트", description: "심플한 레더 벨트.", thumbnailUrl: "", price: 35000, stock: 50, status: "ON_SALE" },
];

const MOCK_RANKING_DAILY = [101, 201, 301, 401, 501, 202, 601, 302, 103, 401].map((id, i) => ({
  rank: i + 1,
  ...MOCK_PRODUCTS.find((p) => p.id === id),
}));

const MOCK_RANKING_WEEKLY = [201, 101, 401, 301, 601, 202, 501, 602, 302, 402].map((id, i) => ({
  rank: i + 1,
  ...MOCK_PRODUCTS.find((p) => p.id === id),
}));

const MOCK_USER = {
  email: "user@example.com",
  name: "이재석",
  phoneNumber: "010-1234-5678",
};

const MOCK_ADDRESSES = [
  {
    id: 1,
    alias: "집",
    name: "이재석",
    phoneNumber: "010-1234-5678",
    zipCode: "06236",
    basicAddress: "서울특별시 강남구 테헤란로 123",
    detailAddress: "9층",
    isDefault: true,
  },
  {
    id: 2,
    alias: "회사",
    name: "이재석",
    phoneNumber: "010-1234-5678",
    zipCode: "04524",
    basicAddress: "서울특별시 중구 세종대로 110",
    detailAddress: "10층 스파르타 사무실",
    isDefault: false,
  },
];

const MOCK_CART = {
  cartId: 1,
  items: [
    { cartItemId: 1, productId: 201, productName: "오버핏 코튼 티셔츠", thumbnailUrl: "", price: 29000, quantity: 2, lineAmount: 58000, status: "판매중" },
    { cartItemId: 2, productId: 301, productName: "와이드 데님 팬츠", thumbnailUrl: "", price: 59000, quantity: 1, lineAmount: 59000, status: "판매중" },
    { cartItemId: 3, productId: 401, productName: "레더 스니커즈", thumbnailUrl: "", price: 99000, quantity: 1, lineAmount: 99000, status: "판매중" },
  ],
  get totalAmount() {
    return this.items.reduce((sum, item) => sum + item.lineAmount, 0);
  },
};

const ORDER_STATUS_LABEL = {
  ORDERED: { text: "결제대기", cls: "badge-warning" },
  COMPLETED: { text: "주문완료", cls: "badge-success" },
  CANCELED: { text: "주문취소", cls: "badge-neutral" },
};

const PAYMENT_STATUS_LABEL = {
  PENDING: { text: "결제대기", cls: "badge-warning" },
  PAID: { text: "결제완료", cls: "badge-success" },
  FAILED: { text: "결제실패", cls: "badge-danger" },
  CANCELED: { text: "결제취소", cls: "badge-neutral" },
};

const PRODUCT_STATUS_LABEL = {
  ON_SALE: { text: "판매중", cls: "badge-success" },
  SOLD_OUT: { text: "품절", cls: "badge-danger" },
  HIDDEN: { text: "숨김", cls: "badge-neutral" },
};

const MOCK_ORDERS = [
  {
    orderId: 1001,
    orderNumber: "ORD-20260810-0001",
    status: "COMPLETED",
    totalProductAmount: 88000,
    createdAt: "2026-08-10T14:22:00",
    recipientName: "이재석",
    recipientPhone: "010-1234-5678",
    zipCode: "06236",
    address1: "서울특별시 강남구 테헤란로 123",
    address2: "9층",
    orderItems: [
      { productId: 201, productName: "오버핏 코튼 티셔츠", unitPrice: 29000, quantity: 1, lineAmount: 29000 },
      { productId: 301, productName: "와이드 데님 팬츠", unitPrice: 59000, quantity: 1, lineAmount: 59000 },
    ],
  },
  {
    orderId: 1002,
    orderNumber: "ORD-20260815-0002",
    status: "ORDERED",
    totalProductAmount: 99000,
    createdAt: "2026-08-15T09:05:00",
    recipientName: "이재석",
    recipientPhone: "010-1234-5678",
    zipCode: "06236",
    address1: "서울특별시 강남구 테헤란로 123",
    address2: "9층",
    orderItems: [
      { productId: 401, productName: "레더 스니커즈", unitPrice: 99000, quantity: 1, lineAmount: 99000 },
    ],
  },
  {
    orderId: 1003,
    orderNumber: "ORD-20260818-0003",
    status: "CANCELED",
    totalProductAmount: 189000,
    createdAt: "2026-08-18T18:40:00",
    recipientName: "이재석",
    recipientPhone: "010-1234-5678",
    zipCode: "06236",
    address1: "서울특별시 강남구 테헤란로 123",
    address2: "9층",
    orderItems: [
      { productId: 101, productName: "오버사이즈 울 코트", unitPrice: 189000, quantity: 1, lineAmount: 189000 },
    ],
  },
];

const MOCK_PAYMENTS = [
  {
    paymentId: 501,
    orderId: 1001,
    portonePaymentId: "portone_abc123",
    status: "PAID",
    totalProductAmount: 88000,
    paymentAmount: 88000,
    items: [
      { productName: "오버핏 코튼 티셔츠", quantity: 1 },
      { productName: "와이드 데님 팬츠", quantity: 1 },
    ],
    approvedAt: "2026-08-10T14:23:10",
    canceledAt: null,
    createdAt: "2026-08-10T14:22:30",
  },
  {
    paymentId: 502,
    orderId: 1003,
    portonePaymentId: "portone_def456",
    status: "CANCELED",
    totalProductAmount: 189000,
    paymentAmount: 189000,
    items: [{ productName: "오버사이즈 울 코트", quantity: 1 }],
    approvedAt: "2026-08-18T18:41:00",
    canceledAt: "2026-08-18T19:02:00",
    createdAt: "2026-08-18T18:40:20",
  },
];

function formatPrice(value) {
  return value.toLocaleString("ko-KR") + "원";
}

function formatDate(iso) {
  if (!iso) return "-";
  const d = new Date(iso);
  const pad = (n) => String(n).padStart(2, "0");
  return `${d.getFullYear()}.${pad(d.getMonth() + 1)}.${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
}
