# Student-Course-Teacher Management System

## 📚 Overview
The Student-Course-Teacher Management System is a comprehensive web application built with Spring Boot that facilitates the management of educational processes in an institution. The system provides a user-friendly interface for managing students, courses, teachers, and their relationships.

## ✨ Features

### 🔐 Authentication
- Simple login and registration system
- Role-based user management (Student, Teacher, Admin)
- Session management using HttpSession

### 👨‍🎓 Students Management
- Add, view, edit, and delete student information
- Enroll students in courses
- View enrolled courses for each student

### 📖 Courses Management
- Create, view, edit, and delete courses
- Assign teachers to courses
- Enroll students in courses
- View course details with enrolled students and assigned teacher

### 👨‍🏫 Teachers Management
- Add, view, edit, and delete teacher information
- Assign courses to teachers
- View assigned courses for each teacher

## 🛠️ Technology Stack

### Backend
- Java 21
- Spring Boot 3.1.5
- Spring MVC
- Spring Data JPA
- Hibernate

### Frontend
- JSP (JavaServer Pages)
- Bootstrap 5
- Font Awesome

### Database
- PostgreSQL

### Tools & Libraries
- Maven
- Flyway (Database Migration)
- Lombok (Reducing boilerplate code)
- JUnit 5 (Testing)
- Mockito (Testing)

## 📊 Entity Relationships

### Student
- Has many-to-many relationship with Course (enrolledCourses)

### Course
- Has many-to-one relationship with Teacher
- Has many-to-many relationship with Student

### Teacher
- Has one-to-many relationship with Course

### User
- Represents system users with roles (STUDENT, TEACHER, ADMIN)
- Connected to Student/Teacher entities based on role

## 🚀 Setup and Installation

### Prerequisites
- Java 21 or higher
- Maven
- PostgreSQL

### Database Setup
1. Create a PostgreSQL database named `schooldb`
2. Update database credentials in `application.properties` if needed

### Installation Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```

2. Navigate to the project directory:
   ```bash
   cd student-course-teacher-management
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

5. Access the application at:
   ```
   http://localhost:8080
   ```

## 📝 Usage

### Default Admin Account
- Username: admin
- Password: admin123

### Registration
1. Navigate to the registration page
2. Fill in the required information
3. Select a role (Student or Teacher)
4. Submit the form

### Managing Students
1. Navigate to the Students section
2. Add new students or edit existing ones
3. View student details to manage course enrollments

### Managing Courses
1. Navigate to the Courses section
2. Add new courses or edit existing ones
3. Assign teachers and enroll students through the course details page

### Managing Teachers
1. Navigate to the Teachers section
2. Add new teachers or edit existing ones
3. Assign courses through the teacher details page

### Error Handling and Custom Error Pages
The application includes a comprehensive error handling system:

#### Custom Error Pages
- 404 (Not Found): Displayed when a resource is not found or when accessing invalid IDs
- 403 (Forbidden): Displayed when access to a resource is denied
- 400 (Bad Request): Displayed when form validation fails
- 500 (Internal Server Error): Displayed when an unexpected server error occurs
- General error page: Displayed for other types of errors

#### Exception Handling
The application uses Spring's `@ControllerAdvice` to handle exceptions globally:
- `IllegalArgumentException`: Returns a 404 Not Found status code (used for non-existent resources)
- `BindException`: Returns a 400 Bad Request status code (used for validation errors)
- Other exceptions: Returns a 500 Internal Server Error status code

This ensures that appropriate HTTP status codes are returned to the client instead of generic 500 errors.

#### Testing Error Pages
For development and testing purposes, you can use the following endpoints:
- `/test-error/404`: Triggers a 404 Not Found error
- `/test-error/403`: Triggers a 403 Forbidden error
- `/test-error/500`: Triggers a 500 Internal Server Error
- `/test-error/error`: Triggers a generic error
- `/test-error/validation`: Tests form validation error handling

Note: These test endpoints should be disabled or removed in production.

## 🧪 Testing
The application includes comprehensive tests for repositories, services, and controllers.

Run tests using:
```bash
mvn test
```

## 📁 Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── softserve/
│   │           └── academy/
│   │               ├── controller/    # MVC Controllers
│   │               ├── dto/           # Data Transfer Objects
│   │               ├── mapper/        # Entity-DTO Mappers
│   │               ├── model/         # JPA Entities
│   │               ├── repository/    # Spring Data Repositories
│   │               ├── service/       # Business Logic Services
│   │               └── Application.java  # Main Application Class
│   ├── resources/
│   │   ├── db/
│   │   │   └── migration/  # Flyway Database Migrations
│   │   └── application.properties  # Application Configuration
│   └── webapp/
│       └── WEB-INF/
│           └── views/  # JSP Views
└── test/
    ├── java/  # Test Classes
    └── resources/  # Test Configuration
```

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
