<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
  <jsp:include page="_meta.jsp"/>
  <title>Đăng ký</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css">
</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-content">
  <div class="card card-custom-width">
    <div class="card-body">
      <c:if test="${not empty requestScope.successMessage}">
        <div class="alert alert-success" role="alert">${requestScope.successMessage}</div>
      </c:if>
      <c:if test="${not empty requestScope.errorMessage}">
        <div class="alert alert-danger" role="alert">${requestScope.errorMessage}</div>
      </c:if>

      <h4 class="card-title mb-4">Đăng ký</h4>

      <form action="${pageContext.request.contextPath}/signup" method="post">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

        <div class="mb-3">
          <label for="inputUsername" class="form-label">Tên đăng nhập</label>
          <input type="text"
                 class="form-control ${not empty requestScope.violations.usernameViolations
                   ? 'is-invalid' : (not empty requestScope.values.username ? 'is-valid' : '')}"
                 id="inputUsername"
                 name="username"
                 value="${requestScope.values.username}">
          <c:if test="${not empty requestScope.violations.usernameViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.usernameViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>

        <div class="mb-3">
          <label for="inputPassword" class="form-label">Mật khẩu</label>
          <input type="password"
                 class="form-control ${not empty requestScope.violations.passwordViolations
                   ? 'is-invalid' : (not empty requestScope.values.password ? 'is-valid' : '')}"
                 id="inputPassword"
                 name="password"
                 value="${requestScope.values.password}">
          <c:if test="${not empty requestScope.violations.passwordViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.passwordViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>

        <div class="mb-3">
          <label for="inputFullname" class="form-label">Họ và tên</label>
          <input type="text"
                 class="form-control ${not empty requestScope.violations.fullnameViolations
                   ? 'is-invalid' : (not empty requestScope.values.fullname ? 'is-valid' : '')}"
                 id="inputFullname"
                 name="fullname"
                 value="${requestScope.values.fullname}">
          <c:if test="${not empty requestScope.violations.fullnameViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.fullnameViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>

        <div class="mb-3">
          <label for="inputEmail" class="form-label">Email</label>
          <input type="email"
                 class="form-control ${not empty requestScope.violations.emailViolations
                   ? 'is-invalid' : (not empty requestScope.values.email ? 'is-valid' : '')}"
                 id="inputEmail"
                 name="email"
                 value="${requestScope.values.email}">
          <c:if test="${not empty requestScope.violations.emailViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.emailViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>

        <div class="mb-3">
          <label for="inputPhoneNumber" class="form-label">Số điện thoại</label>
          <input type="text"
                 class="form-control ${not empty requestScope.violations.phoneNumberViolations
                   ? 'is-invalid' : (not empty requestScope.values.phoneNumber ? 'is-valid' : '')}"
                 id="inputPhoneNumber"
                 name="phoneNumber"
                 value="${requestScope.values.phoneNumber}">
          <c:if test="${not empty requestScope.violations.phoneNumberViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.phoneNumberViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>

        <div class="mb-3">
          <div class="form-check d-inline-block me-4">
            <input class="form-check-input ${not empty requestScope.violations.genderViolations
                     ? 'is-invalid' : (not empty requestScope.values.gender ? 'is-valid' : '')}"
                   type="radio"
                   name="gender"
                   id="radioGender1"
                   value="0"
            ${requestScope.values.gender eq "0" ? 'checked' : ''}>
            <label class="form-check-label" for="radioGender1">Nam</label>
          </div>
          <div class="form-check d-inline-block">
            <input class="form-check-input ${not empty requestScope.violations.genderViolations
                     ? 'is-invalid' : (not empty requestScope.values.gender ? 'is-valid' : '')}"
                   type="radio"
                   name="gender"
                   id="radioGender2"
                   value="1"
            ${requestScope.values.gender eq "1" ? 'checked' : ''}>
            <label class="form-check-label" for="radioGender2">Nữ</label>
          </div>
          <c:if test="${not empty requestScope.violations.genderViolations}">
            <div class="invalid-feedback d-block">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.genderViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>

        <div class="mb-3">
          <label for="inputAddress" class="form-label">Địa chỉ</label>
          <input type="text"
                 class="form-control ${not empty requestScope.violations.addressViolations
                   ? 'is-invalid' : (not empty requestScope.values.address ? 'is-valid' : '')}"
                 id="inputAddress"
                 name="address"
                 value="${requestScope.values.address}">
          <c:if test="${not empty requestScope.violations.addressViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.addressViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>

        <div class="mb-3 form-check">
          <input class="form-check-input ${not empty requestScope.violations.policyViolations
                   ? 'is-invalid' : ''}"
                 type="checkbox"
                 id="checkboxPolicy"
                 name="policy"
          ${requestScope.values.policy eq "on" ? 'checked' : ''}>
          <label class="form-check-label" for="checkboxPolicy">
            Tôi đồng ý với <a href="${pageContext.request.contextPath}/privacy.html" target="_blank">chính sách bảo mật</a>
          </label>
          <c:if test="${not empty requestScope.violations.policyViolations}">
            <div class="invalid-feedback d-block">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.policyViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>

        <button type="submit" class="btn btn-register">Đăng ký</button>
      </form>

      <div class="mt-3 text-center">
        <small>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></small>
      </div>
    </div>
  </div>
</section>

<jsp:include page="_footer.jsp"/>
</body>
</html>
