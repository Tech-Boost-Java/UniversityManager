<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Validation - School Management System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            padding-top: 50px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 30px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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
        <h1 class="mb-4">Test Validation</h1>
        
        <c:if test="${param.success != null}">
            <div class="alert alert-success" role="alert">
                Form submitted successfully!
            </div>
        </c:if>
        
        <p class="mb-4">This form is used to test validation error handling. Submit the form without filling in the required fields to trigger a validation error.</p>
        
        <form action="<c:url value='/test-error/validation'/>" method="post">
            <div class="mb-3">
                <label for="name" class="form-label">Name (required)</label>
                <input type="text" class="form-control" id="name" name="name">
            </div>
            
            <div class="mb-3">
                <label for="email" class="form-label">Email (required)</label>
                <input type="email" class="form-control" id="email" name="email">
            </div>
            
            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary">Submit</button>
                <a href="<c:url value='/dashboard'/>" class="btn btn-secondary">Back to Dashboard</a>
            </div>
        </form>
    </div>
    
    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>