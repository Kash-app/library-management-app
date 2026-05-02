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

    <c:set var="actionUrl" value="${pageContext.request.contextPath}/authors"/>
    <c:if test="${not empty author.id}">
        <c:set var="actionUrl" value="${pageContext.request.contextPath}/authors/${author.id}/edit"/>
    </c:if>

    <form:form action="${actionUrl}" method="post" modelAttribute="author" cssClass="card-form">

        <div class="form-group">
            <form:label path="name">Full Name</form:label>
            <form:input path="name" cssClass="form-control" placeholder="e.g. Jane Austen"/>
            <form:errors path="name" cssClass="field-error"/>
        </div>

        <div class="form-group">
            <form:label path="email">Email Address</form:label>
            <form:input path="email" cssClass="form-control" type="email" placeholder="e.g. author@example.com"/>
            <form:errors path="email" cssClass="field-error"/>
        </div>

        <div class="form-row">
            <div class="form-group half">
                <form:label path="nationality">Nationality</form:label>
                <form:input path="nationality" cssClass="form-control" placeholder="e.g. British"/>
                <form:errors path="nationality" cssClass="field-error"/>
            </div>

            <div class="form-group half">
                <form:label path="birthYear">Birth Year</form:label>
                <form:input path="birthYear" cssClass="form-control" type="number" placeholder="e.g. 1775"/>
                <form:errors path="birthYear" cssClass="field-error"/>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Author</button>
            <a href="${pageContext.request.contextPath}/authors" class="btn btn-secondary">Cancel</a>
        </div>

    </form:form>
</main>
</body>
</html>
