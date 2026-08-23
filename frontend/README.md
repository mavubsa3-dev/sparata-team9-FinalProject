# 9신사 프론트엔드

빌드 도구 없는 순수 HTML/CSS/JS 프론트엔드입니다. 모든 화면이 로컬 백엔드
(`http://localhost:8080`)와 실제로 연동되어 있습니다.

## 실행 방법

1. 백엔드를 로컬에서 `localhost:8080`으로 띄웁니다 (CORS가 `http://localhost:5500`
   origin을 이미 허용하고 있어 별도 설정이 필요 없습니다).
2. 프론트는 상대경로가 꼬이지 않도록 로컬 서버로 띄웁니다.

```bash
cd frontend
python3 -m http.server 5500
# http://localhost:5500 접속
```

백엔드 주소가 다르면 `js/api.js`의 `API_BASE_URL`만 바꿔주면 됩니다.

## 구조

```
frontend/
├── css/
│   ├── reset.css        # 브라우저 기본 스타일 리셋
│   ├── variables.css     # 색상/spacing/폰트 등 디자인 토큰
│   ├── layout.css        # 헤더/푸터/공통 페이지 레이아웃
│   ├── components.css    # 버튼/폼/카드/뱃지/테이블/모달 등 공통 컴포넌트
│   └── admin.css         # 관리자 사이드바 레이아웃
├── js/
│   ├── mock-data.js      # 상태 라벨/포맷터/썸네일 플레이스홀더 등 공통 유틸
│   ├── api.js             # 실제 API 호출 레이어 (모든 화면이 이걸 사용)
│   └── main.js            # 공통 헤더/카테고리바/로그인 상태/토스트 등
├── index.html ~ ranking.html   # 고객용 11개 화면
└── admin/                       # 관리자용 4개 화면
```

빌드 툴 없이 페이지마다 헤더/푸터 마크업을 그대로 복사해 넣었습니다.
공통 컴포넌트를 바꿀 땐 `css/*.css` 만 고치면 되지만, 헤더/푸터 마크업
구조 자체를 바꿀 땐 각 HTML 파일에서 함께 수정해야 합니다.

## 로그인 / 관리자 계정

- 일반 회원은 `signup.html`에서 직접 가입하거나 기존 계정으로 로그인합니다.
- 관리자 화면(`admin/`)은 별도 로그인 페이지 없이 `login.html`을 그대로
  사용합니다. 시드 데이터의 관리자 계정(`admin@admin.com` / `admin1234!`)으로
  로그인한 뒤 `admin/index.html`로 이동하면 됩니다.

## 알려진 제약사항

- **결제 실승인은 안 됩니다.** `GET /api/config/portone`이 `storeId`/`channelKey`를
  반환하지 않는 동안은 주문·결제 레코드는 실제로 생성되지만, PortOne 카드/간편결제
  승인창은 뜨지 않습니다 (`checkout.html`에 안내 배너로 표시).
- **상품 상세/주문 응답에는 카테고리 ID가 없습니다.** `GetOneProductResponse`,
  `GetOrderDetailResponse` 등에 categoryId가 없어 일부 화면(`product-detail.html`)은
  카테고리명을 표시하지 않습니다.
- **인기 상품 랭킹은 결제 완료 이벤트 기반이라 로컬에서는 비어있을 수 있습니다.**
  실제로 결제가 완료된 이력이 없으면 `ranking.html`과 홈 화면의 랭킹 섹션이
  빈 상태 문구만 보여줍니다.

## 응답 형태가 바뀌면

백엔드 PR이 머지되며 응답 DTO 필드가 바뀌면 `js/api.js`의 해당 함수와,
그 함수를 호출하는 페이지의 `<script>` 렌더링 부분만 맞춰 고치면 됩니다.
