# Calendar Task Manager

A full-stack task management application designed for efficient organization of daily activities through calendar-based scheduling.

The application allows users to create, manage and track tasks while providing authentication, profile management and recurring task scheduling.

## Features

### Authentication & Security
- User registration and login
- JWT authentication
- Spring Security integration
- Password encryption
- Role-based authorization

### User Management
- User profiles
- Profile image upload
- Personalized task dashboard

### Task Management
- Create tasks
- Update task details
- Delete tasks
- View tasks by date
- View upcoming task
- View tasks for the next 7 days
- Weekly recurring tasks
- Task status tracking

### Calendar Functionality
- Daily task overview
- Date-based filtering
- Time-based task sorting
- Upcoming event detection

## Technologies

### Backend
- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Maven

### Frontend
- React
- JavaScript
- HTML
- CSS

### Tools
- Postman
- Git
- GitHub

## Architecture

The application follows a layered architecture:

- Controllers
- Services
- Repositories
- Database Layer

Authentication is implemented using JWT tokens and secured endpoints through Spring Security.

## Main Functionalities

### User Features
- Register account
- Login securely
- Upload profile picture
- Manage personal tasks

### Task Features
- Create task
- Edit task
- Delete task
- Search tasks by date
- View next upcoming task
- Display tasks for the next 7 days
- Create recurring weekly tasks

## Database Design

### User
- id
- firstname
- lastname
- username
- email
- password
- profilePicture

### Task
- id
- name
- description
- dateOfStart
- timeOfStart
- dateOfEnd
- timeOfEnd
- status

### Role
- id
- roleName

Relationships:
- One User → Many Tasks
- Many Users → Many Roles

## Future Improvements

- Email notifications
- Task reminders
- Mobile responsive improvements
- Drag & Drop calendar scheduling
- AWS deployment
- Real-time notifications

## Author

Rajan Dz
