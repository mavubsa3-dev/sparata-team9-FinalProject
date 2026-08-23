/**
 * 백엔드 연동용 API 레이어.
 *
 * EC2에 배포된 백엔드(43.203.244.27:8080)를 바라봅니다. 로컬 백엔드로 다시
 * 테스트하려면 이 값을 "http://localhost:8080/api"로 바꾸면 됩니다.
 */

const API_BASE_URL = "http://3.39.20.51:8080/api";

// accessToken 만료로 401을 받았을 때 refreshToken으로 재발급을 시도한다.
// 여러 요청이 동시에 401을 맞아도 재발급은 한 번만 실행되도록 진행 중인 Promise를 공유한다.
let refreshPromise = null;

async function refreshAccessToken() {
  const refreshToken = localStorage.getItem("refreshToken");
  if (!refreshToken) throw new Error("no refresh token");

  const res = await fetch(`${API_BASE_URL}/auth/reissue`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ refreshToken }),
  });
  if (!res.ok) throw new Error("refresh failed");

  const { accessToken, refreshToken: newRefreshToken } = await res.json();
  localStorage.setItem("accessToken", accessToken);
  localStorage.setItem("refreshToken", newRefreshToken);
  return accessToken;
}

function clearAuthAndNotify() {
  const wasLoggedIn = !!localStorage.getItem("accessToken");
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  if (wasLoggedIn) {
    document.dispatchEvent(new CustomEvent("auth:expired"));
  }
}

async function apiFetch(path, options = {}, _isRetry = false) {
  const accessToken = localStorage.getItem("accessToken");
  const res = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
      ...options.headers,
    },
  });

  // accessToken이 만료되어 401이 온 경우: refreshToken으로 재발급 후 한 번만 재시도한다.
  if (res.status === 401 && accessToken && !_isRetry && !path.startsWith("/auth/")) {
    try {
      refreshPromise = refreshPromise || refreshAccessToken().finally(() => {
        refreshPromise = null;
      });
      await refreshPromise;
      return apiFetch(path, options, true);
    } catch (e) {
      clearAuthAndNotify();
      throw { code: "UNAUTHORIZED", message: "로그인이 만료되었습니다. 다시 로그인해주세요." };
    }
  }

  if (!res.ok) {
    const error = await res.json().catch(() => ({ code: "UNKNOWN", message: res.statusText }));
    throw error; // ErrorResponse { code, message }
  }
  if (res.status === 204) return null;
  return res.json();
}

const Api = {
  // 인증
  signup: (body) => apiFetch("/auth/signup", { method: "POST", body: JSON.stringify(body) }),
  login: (body) => apiFetch("/auth/login", { method: "POST", body: JSON.stringify(body) }),
  reissue: (body) => apiFetch("/auth/reissue", { method: "POST", body: JSON.stringify(body) }),
  logout: () => apiFetch("/auth/logout", { method: "POST" }),

  // 사용자
  getMe: () => apiFetch("/user/me"),
  updateMe: (body) => apiFetch("/user/me", { method: "PATCH", body: JSON.stringify(body) }),
  getAddresses: () => apiFetch("/addresses"),
  createAddress: (body) => apiFetch("/addresses", { method: "POST", body: JSON.stringify(body) }),
  updateAddress: (id, body) => apiFetch(`/addresses/${id}`, { method: "PATCH", body: JSON.stringify(body) }),
  deleteAddress: (id) => apiFetch(`/addresses/${id}`, { method: "DELETE" }),

  // 상품 / 카테고리
  getProducts: (categoryId) => apiFetch(`/products${categoryId ? `?categoryId=${categoryId}` : ""}`),
  getProduct: (id) => apiFetch(`/products/${id}`),
  getCategories: () => apiFetch("/categories"),

  // 장바구니
  getCart: () => apiFetch("/carts"),
  getCartCount: () => apiFetch("/carts/count"),
  addCartItem: (body) => apiFetch("/carts/items", { method: "POST", body: JSON.stringify(body) }),
  updateCartItem: (id, body) => apiFetch(`/carts/items/${id}`, { method: "PATCH", body: JSON.stringify(body) }),
  deleteCartItem: (id) => apiFetch(`/carts/items/${id}`, { method: "DELETE" }),
  clearCart: () => apiFetch("/carts/clear", { method: "DELETE" }),

  // 주문
  createOrder: (body) => apiFetch("/orders", { method: "POST", body: JSON.stringify(body) }),
  getOrders: () => apiFetch("/orders"),
  getOrder: (id) => apiFetch(`/orders/${id}`),
  cancelOrder: (id) => apiFetch(`/orders/${id}/cancel`, { method: "PATCH" }),
  setOrderAddress: (id, body) => apiFetch(`/orders/${id}/address`, { method: "PATCH", body: JSON.stringify(body) }),

  // 결제 (PortOne 위젯 연동 전까지는 결제 생성까지만 사용)
  createPayment: (body) => apiFetch("/payments", { method: "POST", body: JSON.stringify(body) }),
  confirmPayment: (id, body) => apiFetch(`/payments/${id}/confirm`, { method: "PATCH", body: JSON.stringify(body) }),
  getPayments: () => apiFetch("/payments"),
  getPayment: (id) => apiFetch(`/payments/${id}`),
  cancelPayment: (id) => apiFetch(`/payments/${id}/cancel`, { method: "PATCH" }),

  // 랭킹
  getRankingDaily: (count) => apiFetch(`/products/ranking?count=${count}`),
  getRankingWeekly: (count) => apiFetch(`/products/ranking/week?count=${count}`),

  // PortOne 설정
  getPortoneConfig: () => apiFetch("/config/portone"),

  // 관리자
  getAdminCategories: () => apiFetch("/admin/categories"),
  createAdminCategory: (body) => apiFetch("/admin/categories", { method: "POST", body: JSON.stringify(body) }),
  updateAdminCategory: (id, body) => apiFetch(`/admin/categories/${id}`, { method: "PATCH", body: JSON.stringify(body) }),
  deleteAdminCategory: (id) => apiFetch(`/admin/categories/${id}`, { method: "DELETE" }),
  getAdminProducts: (query) => apiFetch(`/admin/products${query ? `?${query}` : ""}`),
  createAdminProduct: (body) => apiFetch("/admin/products", { method: "POST", body: JSON.stringify(body) }),
  updateAdminProduct: (id, body) => apiFetch(`/admin/products/${id}`, { method: "PATCH", body: JSON.stringify(body) }),
  deleteAdminProduct: (id) => apiFetch(`/admin/products/${id}`, { method: "DELETE" }),
  getAdminPayments: () => apiFetch("/admin/payments"),
  getAdminPayment: (id) => apiFetch(`/admin/payments/${id}`),

  // 상품 이미지 업로드 (multipart/form-data라 apiFetch를 쓰지 않고 별도 처리)
  uploadProductImage: async (file) => {
    const formData = new FormData();
    formData.append("file", file);
    const accessToken = localStorage.getItem("accessToken");
    const res = await fetch(`${API_BASE_URL}/admin/products/images`, {
      method: "POST",
      headers: accessToken ? { Authorization: `Bearer ${accessToken}` } : {},
      body: formData,
    });
    if (!res.ok) {
      const error = await res.json().catch(() => ({ code: "UNKNOWN", message: res.statusText }));
      throw error;
    }
    return res.json();
  },
};
