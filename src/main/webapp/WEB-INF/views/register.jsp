<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - School Management System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            padding-top: 50px;
        }
        .register-container {
            max-width: 500px;
            margin: 0 auto;
            padding: 30px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .register-header h2 {
            color: #343a40;
        }
        .form-error {
            color: #dc3545;
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="register-container">
            <div class="register-header">
                <h2>School Management System</h2>
                <p class="text-muted">Create a new account</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>

            <form:form action="/register" method="post" modelAttribute="user">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <form:input path="username" class="form-control" id="username" required="true" />
                    <form:errors path="username" cssClass="form-error" />
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <form:input path="email" type="email" class="form-control" id="email" required="true" />
                    <form:errors path="email" cssClass="form-error" />
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">Last Name</label>
                    <form:input path="lastName" class="form-control" id="lastName" required="true" />
                    <form:errors path="lastName" cssClass="form-error" />
                </div>
                <div class="mb-3">
                    <label for="role" class="form-label">Register as</label>
                    <form:select path="role" class="form-select" id="role" required="true">
                        <form:option value="STUDENT">Student</form:option>
                        <form:option value="TEACHER">Teacher</form:option>
                    </form:select>
                    <form:errors path="role" cssClass="form-error" />
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <form:password path="password" class="form-control" id="password" required="true" />
                    <form:errors path="password" cssClass="form-error" />
                    <div class="form-text">Password must be at least 6 characters long.</div>
                </div>
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Register</button>
                </div>
            </form:form>

            <div class="mt-3 text-center">
                <p>Already have an account? <a href="<c:url value='/login'/>">Login here</a></p>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
