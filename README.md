# 🏨 LuxeStays - Premium Hotel Booking Platform

LuxeStays is a comprehensive Full-Stack hotel management application. It allows guests to browse and book luxury stays while providing administrators with powerful tools to track hotel revenue, occupancy, and guest arrivals.

## 🚀 Tech Stack
* **Backend:** Java 17, Spring Boot 4.0.3
* **Database:** PostgreSQL 15 (Dockerized)
* **Security:** JWT-based authentication and BCrypt password encoding
* **Frontend:** Modern HTML5, CSS3 (Glassmorphism design), and Vanilla JavaScript
* **Documentation:** Swagger UI (OpenAPI 3.0) and JavaDoc
* **Testing:** JUnit 5 and Mockito

## 🛠️ Key Features

### 👤 Customer Features
* **Secure Authentication:** JWT-protected registration and login system.
* **Smart Room Search:** Filter rooms by date availability, capacity, and type (Villa, Resort, Apartment).
* **Booking Management:** Create bookings with automatic price calculation and automated overlap protection.
* **Personal Profile:** Manage active bookings and maintain a personalized "Favorites" list.

### 👑 Admin Features
* **Room Management:** Full CRUD operations for managing hotel properties.
* **Financial Insights:** Calculate total revenue for specific periods using high-precision **BigDecimal**.
* **Occupancy Reports:** Real-time statistics on room utilization rates.
* **Arrival Tracking:** Daily logs of confirmed guest arrivals for efficient front-desk management.

## 🧪 Quality Assurance
The project maintains high code reliability through extensive Unit Testing across all layers:
* **Auth & User:** Validates secure login flows and password update logic.
* **Booking Logic:** Ensures zero-overlap in reservations and correct financial billing.
* **Room Services:** Tests CRUD integrity and filtering accuracy.

## 📖 Documentation
* **Interactive API (Swagger):** Available at `http://localhost:8080/swagger-ui.html` during runtime.
* **Technical JavaDoc:** Detailed class and method documentation can be found in the `docs/index.html` directory.

## 💻 Installation & Running

### 1. Database Setup
Ensure Docker is running, then launch the database container:

```bash
docker-compose up -d
```

*The database is accessible on **localhost:5433** as configured in application.properties.*

### 2. Launch Backend
Run the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

### 3. Launch Frontend
The frontend files are located in the `/frontend` directory.
* Open `index.html` using a local server (e.g., VS Code Live Server at `http://127.0.0.1:5500`).
* **Note:** CORS is pre-configured to allow requests from this local address.

---
**Developed by:** Team 9  
**Date:** March 20, 2026