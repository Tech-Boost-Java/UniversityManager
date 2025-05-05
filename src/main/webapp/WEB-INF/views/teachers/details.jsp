<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Details - School Management System</title>
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
        .teacher-info {
            background-color: #e9ecef;
            padding: 15px;
            border-radius: 5px;
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
            <h1>Teacher Details</h1>
            <p>View and manage teacher information and course assignments</p>
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

        <!-- Teacher Information -->
        <div class="teacher-info">
            <div class="row">
                <div class="col-md-8">
                    <h3>${teacher.firstName} ${teacher.lastName}</h3>
                    <p><strong>Email:</strong> ${teacher.email}</p>
                    <p><strong>ID:</strong> ${teacher.id}</p>
                    <p><strong>Courses:</strong> ${courseCount}</p>
                </div>
                <div class="col-md-4 text-md-end">
                    <a href="<c:url value='/teachers/edit/${teacher.id}'/>" class="btn btn-warning">
                        <i class="fas fa-edit"></i> Edit
                    </a>
                    <a href="<c:url value='/teachers'/>" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Back to List
                    </a>
                </div>
            </div>
        </div>

        <!-- Assigned Courses -->
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">Assigned Courses</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty assignedCourses}">
                        <div class="alert alert-info">This teacher is not assigned to any courses yet.</div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Students</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="course" items="${assignedCourses}">
                                        <tr>
                                            <td>${course.id}</td>
                                            <td>${course.name}</td>
                                            <td>${course.description}</td>
                                            <td>${course.students.size()}</td>
                                            <td>
                                                <a href="<c:url value='/courses/details/${course.id}'/>" class="btn btn-info btn-sm">
                                                    <i class="fas fa-info-circle"></i> View
                                                </a>
                                                <form action="<c:url value='/teachers/${teacher.id}/remove-course'/>" method="post" style="display: inline;">
                                                    <input type="hidden" name="courseId" value="${course.id}">
                                                    <button type="submit" class="btn btn-danger btn-sm" 
                                                            onclick="return confirm('Are you sure you want to remove this course from the teacher?')">
                                                        <i class="fas fa-minus-circle"></i> Remove
                                                    </button>
                                                </form>
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

        <!-- Available Courses for Assignment -->
        <div class="card">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0">Assign Courses</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty availableCourses}">
                        <div class="alert alert-info">No available courses for assignment.</div>
                    </c:when>
                    <c:otherwise>
                        <form action="<c:url value='/teachers/${teacher.id}/assign-course'/>" method="post" class="mb-3">
                            <div class="row g-3 align-items-center">
                                <div class="col-auto">
                                    <label for="courseId" class="col-form-label">Select Course:</label>
                                </div>
                                <div class="col-md-6">
                                    <select name="courseId" id="courseId" class="form-select" required>
                                        <option value="">-- Select a course --</option>
                                        <c:forEach var="course" items="${availableCourses}">
                                            <option value="${course.id}">${course.name} (${course.students.size()} students)</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-auto">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-plus-circle"></i> Assign
                                    </button>
                                </div>
                            </div>
                        </form>
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
