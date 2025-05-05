<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teachers - School Management System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .page-header {
            background-color: #dc3545;
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
                        <a class="nav-link" href="<c:url value='/students'/>">Students</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/courses'/>">Courses</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<c:url value='/teachers'/>">Teachers</a>
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
            <h1>Teachers</h1>
            <p>Manage teachers and their course assignments</p>
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

        <!-- Search and Add Teacher -->
        <div class="row mb-3">
            <div class="col-md-6">
                <form action="<c:url value='/teachers/search'/>" method="get" class="d-flex">
                    <input type="text" name="name" class="form-control me-2" placeholder="Search teachers by name..." value="${searchName}">
                    <button type="submit" class="btn btn-outline-danger">
                        <i class="fas fa-search"></i> Search
                    </button>
                </form>
            </div>
            <div class="col-md-6 text-md-end">
                <a href="<c:url value='/teachers/add'/>" class="btn btn-danger">
                    <i class="fas fa-plus"></i> Add New Teacher
                </a>
            </div>
        </div>

        <!-- Teachers Table -->
        <div class="card">
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty teachers}">
                        <div class="alert alert-info">No teachers found. Please add a teacher.</div>
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
                                        <th>Courses</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="teacher" items="${teachers}">
                                        <tr>
                                            <td>${teacher.id}</td>
                                            <td>${teacher.firstName}</td>
                                            <td>${teacher.lastName}</td>
                                            <td>${teacher.email}</td>
                                            <td>${teacher.courses.size()}</td>
                                            <td>
                                                <a href="<c:url value='/teachers/details/${teacher.id}'/>" class="btn btn-info btn-sm">
                                                    <i class="fas fa-info-circle"></i> Details
                                                </a>
                                                <a href="<c:url value='/teachers/edit/${teacher.id}'/>" class="btn btn-warning btn-sm">
                                                    <i class="fas fa-edit"></i> Edit
                                                </a>
                                                <a href="<c:url value='/teachers/delete/${teacher.id}'/>" class="btn btn-danger btn-sm" 
                                                   onclick="return confirm('Are you sure you want to delete this teacher?')">
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
