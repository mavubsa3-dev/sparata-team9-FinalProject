/**
 * 모든 페이지 공통 동작. 실제 로그인 토큰(localStorage의 accessToken)을 기준으로
 * 헤더 상태와 장바구니 배지를 그립니다.
 */

function isLoggedIn() {
  return !!localStorage.getItem("accessToken");
}

function setAuthTokens(accessToken, refreshToken) {
  localStorage.setItem("accessToken", accessToken);
  localStorage.setItem("refreshToken", refreshToken);
}

function clearAuthTokens() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
}

async function initHeader() {
  const loginArea = document.querySelector("[data-auth-area]");
  const loggedIn = isLoggedIn();

  if (loginArea) {
    loginArea.innerHTML = loggedIn
      ? `<a href="mypage.html">마이페이지</a><button type="button" data-logout-btn>로그아웃</button>`
      : `<a href="login.html">로그인</a>`;

    const logoutBtn = loginArea.querySelector("[data-logout-btn]");
    if (logoutBtn) {
      logoutBtn.addEventListener("click", async () => {
        try {
          await Api.logout();
        } catch (e) {
          // 이미 만료된 토큰이어도 클라이언트 쪽 로그아웃은 진행
        }
        clearAuthTokens();
        showToast("로그아웃 되었습니다.");
        setTimeout(() => (location.href = "index.html"), 400);
      });
    }
  }

  const cartBadge = document.querySelector("[data-cart-badge]");
  if (cartBadge) {
    if (!loggedIn) {
      cartBadge.hidden = true;
    } else {
      try {
        const { count } = await Api.getCartCount();
        cartBadge.textContent = count;
        cartBadge.hidden = count === 0;
      } catch (e) {
        cartBadge.hidden = true;
      }
    }
  }
}

async function initCategoryBar() {
  const bar = document.querySelector("[data-category-bar]");
  if (!bar) return;

  try {
    const categories = await Api.getCategories();
    const activeId = new URLSearchParams(location.search).get("categoryId");
    const chips = [{ id: "", name: "전체" }, ...categories];

    bar.innerHTML = chips
      .map((c) => {
        const isActive = (c.id === "" && !activeId) || String(c.id) === activeId;
        const href = c.id === "" ? "index.html" : `index.html?categoryId=${c.id}`;
        return `<a href="${href}" class="category-chip${isActive ? " is-active" : ""}">${c.name}</a>`;
      })
      .join("");
  } catch (e) {
    bar.innerHTML = `<span class="text-faint" style="font-size:var(--fs-sm);">카테고리를 불러오지 못했습니다.</span>`;
  }
}

function showToast(message) {
  let toast = document.querySelector(".toast");
  if (!toast) {
    toast = document.createElement("div");
    toast.className = "toast";
    document.body.appendChild(toast);
  }
  toast.textContent = message;
  toast.classList.add("is-visible");
  clearTimeout(showToast._timer);
  showToast._timer = setTimeout(() => toast.classList.remove("is-visible"), 1800);
}

/** ErrorResponse({code, message}) 또는 일반 Error를 사용자 메시지로 변환 */
function apiErrorMessage(err, fallback) {
  if (err && typeof err.message === "string") return err.message;
  return fallback || "요청 처리 중 오류가 발생했습니다.";
}

// accessToken이 만료됐고 refreshToken으로도 재발급이 안 될 때 api.js가 이 이벤트를 쏜다.
// 그 전까지는 만료된 토큰이어도 localStorage에 남아있어 로그인된 것처럼 보이는 문제를 막는다.
document.addEventListener("auth:expired", () => {
  showToast("로그인이 만료되었습니다. 다시 로그인해주세요.");
  setTimeout(() => location.reload(), 800);
});

document.addEventListener("DOMContentLoaded", () => {
  initHeader();
  initCategoryBar();
});
