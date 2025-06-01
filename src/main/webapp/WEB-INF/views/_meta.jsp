<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Meta tags -->
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="contextPath" content="${pageContext.request.contextPath}"/>

<c:if test="${not empty sessionScope.currentUser}">
  <meta name="currentUserId" content="${sessionScope.currentUser.id}"/>
</c:if>
<c:if test="${not empty requestScope.product}">
  <meta name="productId" content="${requestScope.product.id}"/>
</c:if>

<!-- Favicon -->
<link href="${pageContext.request.contextPath}/img/logo.ico" rel="icon" type="image/x-icon">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-icons.css">



<!-- Custom CSS -->

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css">


<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.js"></script>
<script src="${pageContext.request.contextPath}/js/header.js" type="module"></script>
