<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Students - School Management System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .page-header {
            background-color: #007bff;
            color: white;
            padding: 20px 0;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/dashboard'/>">School Management System</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/dashboard'/>">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<c:url value='/students'/>">Students</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/courses'/>">Courses</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/teachers'/>">Teachers</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <a href="<c:url value='/logout'/>" class="btn btn-outline-light btn-sm">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <!-- Page Header -->
    <div class="page-header">
        <div class="container">
            <h1>Students</h1>
            <p>Manage student information and course enrollments</p>
        </div>
    </div>

    <!-- Main Content -->
    <div class="container">
        <!-- Success Message -->
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- Add Student Button -->
        <div class="mb-3">
            <a href="<c:url value='/students/add'/>" class="btn btn-primary">
                <i class="fas fa-plus"></i> Add New Student
            </a>
        </div>

        <!-- Students Table -->
        <div class="card">
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty students}">
                        <div class="alert alert-info">No students found. Please add a student.</div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>First Name</th>
                                        <th>Last Name</th>
                                        <th>Email</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="student" items="${students}">
                                        <tr>
                                            <td>${student.id}</td>
                                            <td>${student.firstName}</td>
                                            <td>${student.lastName}</td>
                                            <td>${student.email}</td>
                                            <td>
                                                <a href="<c:url value='/students/details/${student.id}'/>" class="btn btn-info btn-sm">
                                                    <i class="fas fa-info-circle"></i> Details
                                                </a>
                                                <a href="<c:url value='/students/edit/${student.id}'/>" class="btn btn-warning btn-sm">
                                                    <i class="fas fa-edit"></i> Edit
                                                </a>
                                                <a href="<c:url value='/students/delete/${student.id}'/>" class="btn btn-danger btn-sm" 
                                                   onclick="return confirm('Are you sure you want to delete this student?')">
                                                    <i class="fas fa-trash"></i> Delete
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-white mt-5 py-3">
        <div class="container text-center">
            <p class="mb-0">&copy; 2025 School Management System. All rights reserved.</p>
        </div>
    </footer>
    
    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>