# 🚀 Course Management Backend (Spring Boot)

A robust backend API for a Course Management System, built with Spring Boot and MySQL. This API provides secure authentication, fully managed CRUD operations, file upload/download, and role-based access suitable for integrating with any frontend.


## 🧠 Tech Stack

- **Spring Boot** (REST API framework)  
- **Spring Security** with JWT for auth  
- **MySQL** (or other RDBMS) as the relational database  
- **Spring Data JPA** for data persistence  
- **Spring MVC** for file upload/download  
- **Lombok** to reduce boilerplate code  
- **Docker Compose** (optional: for easy setup)

## 🔍 Features

- ✅ User registration & JWT-powered login  
- ✅ Role-based routes (User, Instructor, Admin)  
- ✅ CRUD endpoints for Users, Courses, Instructors, Assignments  
- ✅ Secure file upload/download for assignments  
- ✅ Exception handling & validation feedback  
- ✅ CORS configuration to work seamlessly with React frontend  
- ✅ Ready to deploy with Docker Compose


## 🚀 Getting Started

### 1. Clone and Navigate

git clone https://github.com/Pravin-Sonwane-2004/course-management-backend-with-spring-boot.git
cd course-management-backend-with-spring-boot
2. Configure Environment
Create a .env (or .properties) file:


spring.datasource.url=jdbc:mysql://localhost:3306/course_db
spring.datasource.username=root
spring.datasource.password=your_password
jwt.secret=YourJWTSecretKey
jwt.expirationMs=3600000
3. Build & Run

mvn clean install
spring-boot:run
or, with Docker Compose:


docker-compose up --build
Access API: http://localhost:8080/api/

📁 API Endpoints
Module	Endpoint	Method
Auth	/api/auth/register	POST
/api/auth/login	POST
Users	/api/users	GET, GET by ID, POST, PUT, DELETE
Instructors	/api/instructors	GET, POST, PUT, DELETE
Courses	/api/courses	GET, POST, PUT, DELETE
Assignments	/api/assignments	GET, POST, DELETE
Files	/api/assignments/upload	POST
/api/assignments/download/{id}	GET

🛠️ Contributing
Contributions and feedback are welcome! To participate:

⭐ Fork this repo

🧭 Branch:


git checkout -b feature/your-feature
🛠 Implement & test changes

🔃 Submit a clear Pull Request

☕ Support My Work
Enjoying this project? You can support me:


📫 Contact & Links
GitHub (Backend): Pravin‑Sonwane‑2004/course-management-backend-with-spring-boot

GitHub (Frontend): course-management-frontend-with-react

Buy Me a Coffee: https://coff.ee/devpravin

LinkedIn: https://www.linkedin.com/in/pravin-sonwane-079a621ba/
