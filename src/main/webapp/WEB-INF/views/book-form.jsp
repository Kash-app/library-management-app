<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Library — ${formTitle}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <span class="brand">Library Manager</span>
    <a href="${pageContext.request.contextPath}/books">Books</a>
    <a href="${pageContext.request.contextPath}/authors">Authors</a>
</nav>

<main class="container form-page">
    <h1>${formTitle}</h1>

    <c:set var="actionUrl" value="${pageContext.request.contextPath}/books"/>
    <c:if test="${not empty book.id}">
        <c:set var="actionUrl" value="${pageContext.request.contextPath}/books/${book.id}/edit"/>
    </c:if>

    <form:form action="${actionUrl}" method="post" modelAttribute="book" cssClass="card-form">

        <div class="form-group">
            <form:label path="title">Title</form:label>
            <form:input path="title" cssClass="form-control" placeholder="e.g. The Great Gatsby"/>
            <form:errors path="title" cssClass="field-error"/>
        </div>

        <div class="form-group">
            <form:label path="genre">Genre</form:label>
            <form:input path="genre" cssClass="form-control" placeholder="e.g. Literary Fiction"/>
            <form:errors path="genre" cssClass="field-error"/>
        </div>

        <div class="form-row">
            <div class="form-group half">
                <form:label path="publishedYear">Published Year</form:label>
                <form:input path="publishedYear" cssClass="form-control" type="number" placeholder="e.g. 1925"/>
                <form:errors path="publishedYear" cssClass="field-error"/>
            </div>

            <div class="form-group half">
                <form:label path="isbn">ISBN</form:label>
                <form:input path="isbn" cssClass="form-control" placeholder="e.g. 978-0-7432-7356-5"/>
                <form:errors path="isbn" cssClass="field-error"/>
            </div>
        </div>

        <div class="form-group">
            <form:label path="author">Author</form:label>
            <form:select path="author.id" cssClass="form-control">
                <form:option value="" label="-- Select Author --"/>
                <c:forEach var="a" items="${authors}">
                    <form:option value="${a.id}" label="${a.name}"/>
                </c:forEach>
            </form:select>
            <form:errors path="author" cssClass="field-error"/>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Book</button>
            <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">Cancel</a>
        </div>

    </form:form>
</main>
</body>
</html>
