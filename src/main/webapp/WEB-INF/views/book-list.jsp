<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Library — Books</title>
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
        <h1>All Books</h1>
        <a href="${pageContext.request.contextPath}/books/new" class="btn btn-primary">+ Add Book</a>
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
                <th>Title</th>
                <th>Genre</th>
                <th>Year</th>
                <th>ISBN</th>
                <th>Author</th>
                <th>Nationality</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty bookViews}">
                    <tr><td colspan="8" class="empty-msg">No books found. Add one above.</td></tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="bv" items="${bookViews}" varStatus="st">
                        <tr>
                            <td>${st.count}</td>
                            <td>${bv.title}</td>
                            <td><span class="badge">${bv.genre}</span></td>
                            <td>${bv.publishedYear}</td>
                            <td class="mono">${bv.isbn}</td>
                            <td>${bv.authorName}</td>
                            <td>${bv.authorNationality}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/books/${bv.bookId}/edit"
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
