<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - School Management System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .dashboard-header {
            background-color: #343a40;
            color: white;
            padding: 20px 0;
            margin-bottom: 30px;
        }
        .card {
            margin-bottom: 20px;
            transition: transform 0.3s;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }
        .card-icon {
            font-size: 3rem;
            margin-bottom: 15px;
        }
        .students-icon { color: #007bff; }
        .courses-icon { color: #28a745; }
        .teachers-icon { color: #dc3545; }
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
                        <a class="nav-link active" href="<c:url value='/dashboard'/>">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/students'/>">Students</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/courses'/>">Courses</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/teachers'/>">Teachers</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <span class="navbar-text me-3">
                        Welcome, ${username}
                    </span>
                    <a href="<c:url value='/logout'/>" class="btn btn-outline-light btn-sm">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <!-- Dashboard Header -->
    <div class="dashboard-header">
        <div class="container">
            <h1>Welcome to School Management System</h1>
            <p>Manage students, courses, and teachers with ease</p>
        </div>
    </div>

    <!-- Dashboard Content -->
    <div class="container">
        <div class="row">
            <!-- Students Card -->
            <div class="col-md-4">
                <div class="card text-center">
                    <div class="card-body">
                        <div class="card-icon students-icon">
                            <i class="fas fa-user-graduate"></i>
                        </div>
                        <h5 class="card-title">Students</h5>
                        <p class="card-text">Manage student information, enrollments, and course assignments.</p>
                        <a href="<c:url value='/students'/>" class="btn btn-primary">Manage Students</a>
                    </div>
                </div>
            </div>
            
            <!-- Courses Card -->
            <div class="col-md-4">
                <div class="card text-center">
                    <div class="card-body">
                        <div class="card-icon courses-icon">
                            <i class="fas fa-book"></i>
                        </div>
                        <h5 class="card-title">Courses</h5>
                        <p class="card-text">Create and manage courses, assign teachers, and enroll students.</p>
                        <a href="<c:url value='/courses'/>" class="btn btn-success">Manage Courses</a>
                    </div>
                </div>
            </div>
            
            <!-- Teachers Card -->
            <div class="col-md-4">
                <div class="card text-center">
                    <div class="card-body">
                        <div class="card-icon teachers-icon">
                            <i class="fas fa-chalkboard-teacher"></i>
                        </div>
                        <h5 class="card-title">Teachers</h5>
                        <p class="card-text">Add and manage teachers, and assign them to specific courses.</p>
                        <a href="<c:url value='/teachers'/>" class="btn btn-danger">Manage Teachers</a>
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