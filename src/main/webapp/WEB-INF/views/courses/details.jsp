<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Details - School Management System</title>
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
        .course-info {
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
            <h1>Course Details</h1>
            <p>View and manage course information, teacher assignment, and student enrollments</p>
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

        <!-- Course Information -->
        <div class="course-info">
            <div class="row">
                <div class="col-md-8">
                    <h3>${course.name}</h3>
                    <p><strong>Description:</strong> ${course.description}</p>
                    <p><strong>ID:</strong> ${course.id}</p>
                    <p><strong>Teacher:</strong> ${course.teacher != null ? course.teacher.firstName.concat(' ').concat(course.teacher.lastName) : 'Not assigned'}</p>
                </div>
                <div class="col-md-4 text-md-end">
                    <a href="<c:url value='/courses/edit/${course.id}'/>" class="btn btn-warning">
                        <i class="fas fa-edit"></i> Edit
                    </a>
                    <a href="<c:url value='/courses'/>" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Back to List
                    </a>
                </div>
            </div>
        </div>

        <!-- Teacher Assignment -->
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">Teacher Assignment</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${course.teacher != null}">
                        <div class="mb-3">
                            <p><strong>Current Teacher:</strong> ${course.teacher.firstName} ${course.teacher.lastName} (${course.teacher.email})</p>
                            <form action="<c:url value='/courses/${course.id}/assign-teacher'/>" method="post" class="d-inline">
                                <input type="hidden" name="teacherId" value="">
                                <button type="submit" class="btn btn-danger" 
                                        onclick="return confirm('Are you sure you want to remove this teacher from the course?')">
                                    <i class="fas fa-user-minus"></i> Remove Teacher
                                </button>
                            </form>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>No teacher assigned to this course.</p>
                    </c:otherwise>
                </c:choose>

                <form action="<c:url value='/courses/${course.id}/assign-teacher'/>" method="post" class="mt-3">
                    <div class="row g-3 align-items-center">
                        <div class="col-auto">
                            <label for="teacherId" class="col-form-label">Assign Teacher:</label>
                        </div>
                        <div class="col-md-6">
                            <select name="teacherId" id="teacherId" class="form-select" required>
                                <option value="">-- Select a teacher --</option>
                                <c:forEach var="teacher" items="${availableTeachers}">
                                    <option value="${teacher.id}" ${course.teacher != null && course.teacher.id == teacher.id ? 'selected' : ''}>
                                        ${teacher.firstName} ${teacher.lastName} (${teacher.email})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-auto">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-user-plus"></i> Assign
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Enrolled Students -->
        <div class="card mb-4">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0">Enrolled Students</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty enrolledStudents}">
                        <div class="alert alert-info">No students enrolled in this course yet.</div>
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
                                    <c:forEach var="student" items="${enrolledStudents}">
                                        <tr>
                                            <td>${student.id}</td>
                                            <td>${student.firstName}</td>
                                            <td>${student.lastName}</td>
                                            <td>${student.email}</td>
                                            <td>
                                                <form action="<c:url value='/courses/${course.id}/remove-student'/>" method="post" style="display: inline;">
                                                    <input type="hidden" name="studentId" value="${student.id}">
                                                    <button type="submit" class="btn btn-danger btn-sm" 
                                                            onclick="return confirm('Are you sure you want to remove this student from the course?')">
                                                        <i class="fas fa-user-minus"></i> Remove
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

        <!-- Available Students for Enrollment -->
        <div class="card">
            <div class="card-header bg-info text-white">
                <h5 class="mb-0">Add Students to Course</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty availableStudents}">
                        <div class="alert alert-info">No available students for enrollment.</div>
                    </c:when>
                    <c:otherwise>
                        <form action="<c:url value='/courses/${course.id}/add-student'/>" method="post" class="mb-3">
                            <div class="row g-3 align-items-center">
                                <div class="col-auto">
                                    <label for="studentId" class="col-form-label">Select Student:</label>
                                </div>
                                <div class="col-md-6">
                                    <select name="studentId" id="studentId" class="form-select" required>
                                        <option value="">-- Select a student --</option>
                                        <c:forEach var="student" items="${availableStudents}">
                                            <option value="${student.id}">${student.firstName} ${student.lastName} (${student.email})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-auto">
                                    <button type="submit" class="btn btn-info">
                                        <i class="fas fa-user-plus"></i> Add Student
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
