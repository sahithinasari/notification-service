# Notification Service

A production-grade, event-driven Notification Service built with Spring Boot 3 and RabbitMQ, 
supporting notifications through Email with retry handling, DLQ, and Redis-based idempotency.
The service is designed for scalability, fault tolerance, and exactly-once delivery semantics at the business level.

---
## Prerequisites
- **Java 17**
- **Maven 3.8+**
- **Docker & Docker Compose**
- A **Gmail App Password** (for sending emails)

---
## Features
- Asynchronous notification processing using RabbitMQ
- Email-channel delivery
- Topic exchange–based routing for event + channel fan-out
- Retry handling for transient failures (Spring Retry)
- Dead Letter Queues (DLQ) for permanent failures
- Redis-based idempotency to prevent duplicate notifications
- Template-based email rendering using Thymeleaf
- OTP notification support
- Swagger UI for API testing
---
## High-Level Architecture
```
Client
  |
  | POST /api/notifications
  v
Notification API
  |
  | Generate notificationId
  | Publish events
  v
RabbitMQ (Topic Exchange)
  |
  +--> email.queue  --> Email Worker  --> email.dlq.queue
```
## Core Design Principles
### Event-Driven & Asynchronous
- API responds immediately (202 ACCEPTED)
- Notification delivery happens asynchronously via RabbitMQ
### Channel Isolation
- Email, SMS, and Push are processed independently
- Failure in one channel does not affect others
### At-Least-Once Delivery + Idempotency
- RabbitMQ guarantees at-least-once delivery
- Redis idempotency ensures exactly-once delivery at the business level

### RabbitMQ Design
Exchange
- Type: Topic
- Name: notification.events.exchange

Routing Key Pattern
```
notification.<eventType>.<channel>
```

Examples:
```
notification.order_placed.email
```
### Idempotency (Redis)

RabbitMQ provides at-least-once delivery, which can lead to duplicate processing.

Idempotency Key
```
notification:{notificationId}:{channel}
```
Example:
```
notification:abc123:EMAIL
```
Implementation
```
Redis SETNX (setIfAbsent)
TTL: 24 hours
```
Prevents duplicates across retries, crashes, and restarts

---
## Running Locally (with Docker)
1. Build the JAR:
```
mvn clean package -DskipTests
```

2. Build & Start Containers:
```
docker-compose up -d --build
```
- RabbitMQ UI: http://localhost:15672
(guest / guest)
- Application: http://localhost:2024
- Swagger UI: http://localhost:2024/swagger-ui.html

### API Endpoints
Once running, visit:

Swagger UI: http://localhost:2024/swagger-ui.html

Sample Endpoints:
```
POST /api/notifications
```
Request payload:

```
{
  "userId": "U123",
  "eventType": "ORDER_PLACED",
  "channels": ["EMAIL"],
  "recipient": "priyamvadhanasari@gmail.com",
  "params": {
    "orderId": "ORD1001",
    "amount": "1999"
  }
}
```

## Running Without Docker
If you have RabbitMQ installed locally
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


