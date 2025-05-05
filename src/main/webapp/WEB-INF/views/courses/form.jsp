<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${course.id == null ? 'Add' : 'Edit'} Course - School Management System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .page-header {
            background-color: #28a745;
            color: white;
            padding: 20px 0;
            margin-bottom: 20px;
        }
        .form-error {
            color: #dc3545;
            font-size: 0.875rem;
            margin-top: 0.25rem;
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
                        <a class="nav-link active" href="<c:url value='/courses'/>">Courses</a>
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
            <h1>${course.id == null ? 'Add New' : 'Edit'} Course</h1>
            <p>${course.id == null ? 'Create a new course' : 'Update course information'}</p>
        </div>
    </div>

    <!-- Main Content -->
    <div class="container">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <div class="card">
                    <div class="card-body">
                        <form:form action="/courses/save" method="post" modelAttribute="course">
                            <form:hidden path="id" />

                            <div class="mb-3">
                                <label for="name" class="form-label">Course Name</label>
                                <form:input path="name" class="form-control" id="name" />
                                <form:errors path="name" cssClass="form-error" />
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <form:textarea path="description" class="form-control" id="description" rows="3" />
                                <form:errors path="description" cssClass="form-error" />
                            </div>

                            <div class="mb-3">
                                <label for="teacherId" class="form-label">Teacher (Optional)</label>
                                <select name="teacherId" id="teacherId" class="form-select">
                                    <option value="">-- Select a teacher --</option>
                                    <c:forEach var="teacher" items="${teachers}">
                                        <option value="${teacher.id}" ${course.teacher != null && course.teacher.id == teacher.id ? 'selected' : ''}>
                                            ${teacher.firstName} ${teacher.lastName} (${teacher.email})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="<c:url value='/courses'/>" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-times"></i> Cancel
                                </a>
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save"></i> Save
                                </button>
                            </div>
                        </form:form>
                    </div>
                </div>
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
