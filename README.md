
# 🎓 Alumni Social Network

![Alumni Social Network Banner](https://via.placeholder.com/1200x400?text=Alumni+Social+Network)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![React](https://img.shields.io/badge/frontend-react-blue.svg)]()
[![Spring MVC](https://img.shields.io/badge/backend-springboot-green.svg)]()

---

## 📚 Table of Contents

- [Introduction](#-introduction)
- [Demo](#-demo)
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

## 🎥 Demo

Click the image below to watch a short video demonstration of the project:

[![Watch the demo](https://img.youtube.com/vi/YOUTUBE_VIDEO_ID/0.jpg)](https://www.youtube.com/watch?v=YOUTUBE_VIDEO_ID)

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
   ```

2. **Backend Setup**
   ```bash
   cd backend
   # Configure database in application.properties
   nano src/main/resources/application.properties

   # Build and run
   mvn clean install
   mvn spring-boot:run
   ```

3. **Frontend Setup**
   ```bash
   cd frontend
   npm install
   npm start
   ```

4. **Firebase Configuration**
- Create a Firebase project at https://firebase.google.com/
- Add your Firebase config in frontend/src/firebase.js
- Enable Firebase Realtime Database and Authentication

5. **Environment Variables**
- Create a .env file in the backend with:
   ```bash
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/alumni_db
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=yourpassword
   SPRING_MAIL_USERNAME=your-email@gmail.com
   SPRING_MAIL_PASSWORD=your-email-password
   SENTIMENT_API_KEY=your-sentiment-api-key
   ```

---

## 📊 Sentiment Analysis Integration

The system uses **Sentiment API** to analyze comments:

- Comments are analyzed in real-time  
- Negative sentiment comments are flagged  
- Automatic deletion occurs within 24 hours for toxic content  
- Threshold can be adjusted in `SentimentAnalysisService.java`  

---

## 🔌 WebSocket Configuration

Real-time features use **STOMP over WebSocket**:

- **Endpoint:** `/ws`
- **Topics:**
  - `/topic/notifications` – For system notifications  
  - `/user/queue/private` – For private messages  
- Configured in `WebSocketConfig.java`

---

## 📧 Email Service

Configured with **Spring Mail**:

- **SMTP settings:** in `application.properties`  
- **Templates:** in `resources/templates/email/`

**Triggers:**

- Account creation  
- Password reset  
- Event invitations  
- Important notifications  

---

## 📱 Firebase Chat Implementation

### Real-time Chat Architecture
The chat system is built on Firebase's real-time infrastructure with the following components:

![Chat Architecture Diagram](https://via.placeholder.com/600x400?text=Chat+Architecture+Diagram)

### Core Features
- **Instant Messaging**: Real-time message delivery with <100ms latency
- **Message History**: Persisted conversation history with Firebase Firestore
- **Read Receipts**: Visual indicators for message status (sent, delivered, read)
- **Typing Indicators**: Shows when other participants are typing
- **Online Status**: Real-time presence detection
- **Media Support**: Image and file sharing capability
- **Unread Counts**: Badges showing unread messages

### Implementation Details

#### Firebase Configuration
Located in `frontend/src/app/firebaseConfig.js`:
   ```javascript
   import { initializeApp } from "firebase/app";
   import { getFirestore } from "firebase/firestore";

   const firebaseConfig = {
   apiKey: "YOUR_API_KEY",
   authDomain: "your-project.firebaseapp.com",
   projectId: "your-project",
   storageBucket: "your-project.appspot.com",
   messagingSenderId: "1234567890",
   appId: "1:1234567890:web:abcdef123456"
   };

   const app = initializeApp(firebaseConfig);
   export const db = getFirestore(app);
   ```

---

## 📈 ChartJS Integration

Statistics dashboard features:

- User growth over time  
- Post engagement metrics  
- Survey result visualization  
- Responsive chart components  
- Custom date range filtering  

---

## 🤝 Contributing

1. Fork the project  
2. Create your feature branch  
   ```bash
   git checkout -b feature/AmazingFeature
   ```

3. Commit your changes  
   ```bash
   git commit -m "Add AmazingFeature"
   ```

4. Push to the branch  
   ```bash
   git push origin feature/AmazingFeature
   ```

5. Open a Pull Request

---


## 🌟 Demo Accounts

| Role     | Email                   | Password   |
|----------|-------------------------|------------|
| Admin    | admin@university.edu    | admin123   |
| Lecturer | teacher@university.edu  | ou@123     |
| Alumni   | alumni@example.com      | alumni123  |

> ⚠️ Note: Lecturer accounts must change their password within 24 hours after first login.

---

## 📄 API Documentation

After starting the backend server, access Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## 📬 Contact

For questions or support, please contact:

- **Project Lead:** [Cuong Nguyen] – nguyenmanhc261@gmail.com 
- **University IT Department:** ou@ou.edu.vn

---

## 📜 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <sub>Built with ❤︎ by <a href="https://github.com/CatV2004">CatV Pr</a></sub>
</div>
