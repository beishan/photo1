# Photo Management System

A full-stack photo management system with Spring Boot backend and Vue.js frontend.

## Project Structure

- `backend/`: Spring Boot backend project
- `frontend/`: Vue.js frontend project

## Backend

Spring Boot application providing REST APIs for photo management.

### Setup
```bash
# Enter backend directory
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend server will run on http://localhost:8080

### Configuration

Edit `src/main/resources/application.yml` to configure:
- Database connection
- JWT settings
- File storage location

### Setup 