# 🔥 FinTrack — Personal Finance Tracker

> **Backend-driven full-stack application** for managing income, expenses, and financial insights with a production-style deployment setup.

FinTrack is a **full-stack personal finance tracking application** designed to help users manage income and expenses, organize transactions by category, and gain meaningful financial insights through analytics.  
The project places a strong emphasis on **backend architecture, security, data modeling, and real-world deployment**, supported by a clean and responsive React frontend.

---

## 🧠 System Architecture

FinTrack follows a **real-world, production-style architecture** commonly used in modern web applications:

- 🌐 **React.js Frontend** deployed on **Netlify**
- 🔧 **Spring Boot Backend** deployed on **Render**
- 🗄️ **Remote PostgreSQL Database** securely connected to the backend
- 🔁 Frontend communicates with backend through **authenticated api endpoints**


## 🚀 Core Capabilities

### 🧠 Backend (Primary Engineering Focus)

- 🔐 Secure authentication using **JWT**
- 🧾 User account and session lifecycle handling
- 📁 Category-based financial organization
- 💸 Income & expense transaction processing with strong validations
- 📊 Backend-driven dashboard analytics
- 🎯 Advanced filtering by category, date, and transaction type
- ⚡ Stateless, scalable service-layer design
- 🐳 Dockerized backend for consistent deployments
- 🧪 Unit testing with **JUnit**

---

### 🎨 Frontend

- 📱 Responsive UI built with **React.js** and **Tailwind CSS**
- 📝 Smooth authentication flows (Signup & Login)
- 📂 Category, Income, Expense & Filter modules
- 📊 Interactive dashboards with charts
- 🪟 Modals, 😀 Emoji picker & 🔔 toast notifications for better UX

---

## 🛠 Technology Stack

### Backend
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL (Remote Database)
- Docker
- JUnit

### Frontend
- React.js
- Axios
- Tailwind CSS
- Lucide React
- React Hot Toast
- Emoji Picker
- React Charts

---

## 🔐 Backend Design & Security Highlights

- Stateless authentication using **JWT**
- Secured endpoints via **Spring Security filters**
- Strong password hashing and token validation
- Clean layered architecture  
  *(Controller → Service → Repository)*

---

## 🎯 Project Focus

FinTrack is intentionally **backend-heavy**, with major emphasis on:
- Secure authentication flows
- Financial data consistency & validation
- Analytics and aggregation logic
- Real-world deployment and scalability considerations

The frontend acts as a **consumer layer** to demonstrate and validate backend capabilities.



