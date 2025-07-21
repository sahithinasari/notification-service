# Notification Service

A **Spring Boot 3** microservice that handles **email notifications** (welcome emails, password resets, promotions, etc.) using **RabbitMQ for message queuing** and **Thymeleaf-based templates**.  
The service is fully **containerized with Docker** and ready to run locally or in production.

---

## Features
- **RabbitMQ-based messaging** for async notification dispatch.
- **Email sending via Gmail SMTP** (configurable via `.env`).
- **Thymeleaf-based dynamic templates** for easy customization.
- **Swagger UI** for API testing (`/swagger-ui.html`).
- **Docker & Docker Compose** for running the app and RabbitMQ.
---

## Prerequisites
- **Java 17**
- **Maven 3.8+**
- **Docker & Docker Compose**
- A **Gmail App Password** (for sending emails)

---

## Environment Variables

Create a `.env` file in the root directory:

```env
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password 
```

SPRING_PROFILES_ACTIVE=default
⚠️ Do not commit .env to GitHub!
Add it to .gitignore to keep credentials safe.

Running Locally (with Docker)
1. Build the JAR:
mvn clean package -DskipTests

2. Build & Start Containers:
docker-compose up -d --build
This will:
Start RabbitMQ (with management UI at http://localhost:15672 — user guest/guest)

Build and run the notification-service container on port 2024.

API Endpoints
Once running, visit:

Swagger UI: http://localhost:2024/swagger-ui.html

Sample Endpoints:

POST /api/notifications/send – Send a notification.

GET /api/notifications/templates/{type} – Fetch an email template.

Example request:

POST /api/notifications/send
```
{
  "recipient": "user@example.com",
  "type": "WELCOME",
  "variables": {
    "name": "John"
  }
}
```

Testing RabbitMQ
You can test by publishing a message manually via RabbitMQ:

Access RabbitMQ UI at http://localhost:15672 (user: guest, pass: guest).

Create a queue named notification.queue if not already present.

Publish a test message:
```
{
  "recipient": "test@example.com",
  "type": "WELCOME",
  "variables": {
    "name": "Tester"
  }
}

```
Running Without Docker
If you have RabbitMQ installed locally:
Start RabbitMQ:
rabbitmq-server
Run the Spring Boot app directly:
```
mvn spring-boot:run
 ```

The app will be available on http://localhost:2024.

Stopping the Containers

```
docker-compose down
```

---


