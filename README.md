# 🎓 Alumni Social Network

![Alumni Social Network Banner](https://via.placeholder.com/1200x400?text=Alumni+Social+Network)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![React](https://img.shields.io/badge/frontend-react-blue.svg)]()
[![Spring Boot](https://img.shields.io/badge/backend-springboot-green.svg)]()

---

## 📚 Table of Contents

- [Introduction](#-introduction)
- [Key Features](#-key-features)
- [Technology Stack](#-technology-stack)
- [Installation & Run](#-installation--run)
- [Project Structure](#-project-structure)
- [Demo Accounts](#-demo-accounts)
- [API Documentation](#-api-documentation)
- [Contact](#-contact)
- [License](#-license)

---

## 📌 Introduction
**Alumni Social Network** is a dedicated social platform for alumni, lecturers, and university administrators. It offers essential social features such as posts, comments, interactions, as well as advanced features like surveys, statistics, event management, and real-time chat.

---

## ✨ Key Features

### 🔐 Authentication & Role Management
- Three user roles: Alumni, Lecturer, Administrator
- Alumni verification via student ID
- Mandatory password change within the first 24 hours for lecturer accounts
- Password recovery and account restoration

### 👤 Personal Profile
- Timeline displaying user posts in chronological order
- Customizable avatar and cover photo
- Personal information management

### 📢 Social Features
- Create posts with various content types
- React (like, haha, love) and comment on posts
- Comment control: lock or delete comments
- Create surveys and view real-time statistics
- Publish event announcements and send mass emails

### 📊 Statistics & Administration
- Dashboard with user and post statistics over time
- Interactive charts using ChartJS
- Content moderation: delete inappropriate posts

### 💬 Real-Time Chat
- Integrated Firebase for real-time messaging
- Real-time notifications via WebSocket

### 🤖 AI Moderation
- Sentiment analysis integration using Sentiment API
- Auto-delete toxic comments within 24 hours

---

## 🛠 Technology Stack

### Backend
- **Spring MVC**: Core backend framework
- **Spring Security**: Authentication and authorization
- **Spring Mail**: Email notifications
- **WebSocket**: Real-time communication
- **JPA/Hibernate**: Database management

### Frontend
- **ReactJS**: Frontend framework
- **ChartJS**: Data visualization
- **Firebase**: Real-time chat
- **Thymeleaf**: Template engine for the admin panel

### Others
- **Sentiment Analysis API**: Comment sentiment analysis
- **MySQL**: Relational database

---

## 🚀 Installation & Run

### System Requirements
- JDK 11+
- Node.js 14+
- MySQL 8.0+
- Maven 3.6+

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/alumni-social-network.git
   cd alumni-social-network
Configure the database

Create a database named alumni_db in MySQL.

Update your database connection settings in application.properties.

Run the backend

bash
Sao chép
Chỉnh sửa
cd backend
mvn spring-boot:run
Run the frontend

bash
Sao chép
Chỉnh sửa
cd frontend
npm install
npm start
Access the application

Frontend: http://localhost:3000

Admin Panel: http://localhost:8080/admin

📂 Project Structure
csharp
Sao chép
Chỉnh sửa
alumni-social-network/
├── backend/               # Spring Boot application
│   ├── src/
│   ├── pom.xml
│   └── application.properties
├── frontend/              # React application
│   ├── public/
│   ├── src/
│   └── package.json
├── docs/                  # Documentation
└── README.md
🌟 Demo Accounts
Role	Email	Password
Admin	admin@university.edu	admin123
Lecturer	teacher@university.edu	ou@123
Alumni	alumni@example.com	alumni123

Note: Lecturer accounts must change their password upon the first login.

📄 API Documentation
You can access the API documentation via Swagger UI after starting the backend:

bash
Sao chép
Chỉnh sửa
http://localhost:8080/swagger-ui.html
📬 Contact
If you have any questions or would like to contribute, please contact:

Name: [Your Name]
Email: your.email@example.com
Website: https://yourwebsite.com

📜 License
This project is licensed under the MIT License.

<div align="center"> <sub>Built with ❤︎ by <a href="https://github.com/yourusername">Your Name</a></sub> </div> ```