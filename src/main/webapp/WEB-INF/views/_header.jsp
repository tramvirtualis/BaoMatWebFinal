<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header class="section-header">
  <section class="header-main border-bottom">
    <div class="container">
      <div class="row align-items-center">
        <!-- Logo -->
        <div class="col-lg-3 py-3">
          <a class="text-body" href="${pageContext.request.contextPath}/">
            <h3 class="text-white">Shop Bán Đồ chơi</h3>
          </a>
        </div>

        <!-- Search Bar -->
        <div class="col-lg-5 ${empty sessionScope.currentUser ? 'mb-3 mb-lg-0' : ''}">
          <form action="${pageContext.request.contextPath}/search" method="post" class="search">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
            <div class="input-group w-100">
              <input type="text" class="form-control" placeholder="Nhập từ khóa cần tìm ..." name="q" value="${requestScope.query}">
              <button class="btn btn-pink" type="submit">
                <i class="bi bi-search"></i>
              </button>
            </div>
          </form>
        </div>

        <!-- Account and Cart -->
        <div class="col-lg-4">
          <c:if test="${not empty sessionScope.currentUser}">
            <ul class="nav col-12 col-lg-auto my-2 my-lg-0 justify-content-center justify-content-lg-end text-small">
              <li>
                <a href="${pageContext.request.contextPath}/user" class="nav-link text-white">
                  <i class="bi bi-person d-block text-center fs-3"></i>
                  Tài khoản
                </a>
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/order" class="nav-link text-white">
                  <i class="bi bi-list-check d-block text-center fs-3"></i>
                  Đơn hàng
                </a>
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/cart" class="nav-link text-white position-relative">
                  <span id="total-cart-items-quantity" class="position-absolute top-0 end-0 mt-2 badge rounded-pill bg-primary">
                    ...
                  </span>
                  <i class="bi bi-cart d-block text-center fs-3 position-relative"></i>
                  Giỏ hàng
                </a>
              </li>
            </ul>
          </c:if>
        </div>
      </div>
    </div>
  </section>
</header>

<!-- Navbar -->
<nav class="navbar navbar-main navbar-expand-lg navbar-light border-bottom">
  <div class="container">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
            aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <c:choose>
        <c:when test="${not empty sessionScope.currentUser}">
          <span class="me-2 text-white">Xin chào <strong>${sessionScope.currentUser.fullname}</strong>!</span>
          <a class="btn btn-light ms-2" href="${pageContext.request.contextPath}/signout" role="button">
            Đăng xuất
          </a>
        </c:when>
        <c:otherwise>
          <a class="btn btn-warning" href="${pageContext.request.contextPath}/signup" role="button">
            Đăng ký
          </a>
          <a class="btn btn-pink" href="${pageContext.request.contextPath}/signin" role="button">
            Đăng nhập
          </a>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</nav>
