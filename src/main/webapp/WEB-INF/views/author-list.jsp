<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Library — Authors</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <span class="brand">Library Manager</span>
    <a href="${pageContext.request.contextPath}/books">Books</a>
    <a href="${pageContext.request.contextPath}/authors">Authors</a>
</nav>

<main class="container">
    <div class="page-header">
        <h1>All Authors</h1>
        <a href="${pageContext.request.contextPath}/authors/new" class="btn btn-primary">+ Add Author</a>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <table class="data-table">
        <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Email</th>
                <th>Nationality</th>
                <th>Birth Year</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty authors}">
                    <tr><td colspan="6" class="empty-msg">No authors found. Add one above.</td></tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="a" items="${authors}" varStatus="st">
                        <tr>
                            <td>${st.count}</td>
                            <td>${a.name}</td>
                            <td class="mono">${a.email}</td>
                            <td>${a.nationality}</td>
                            <td>${a.birthYear}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/authors/${a.id}/edit"
                                   class="btn btn-sm btn-secondary">Edit</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</main>
</body>
</html>
