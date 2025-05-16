# 🛒 Secure Order Microservice

A production-ready, event-driven microservice for managing orders in an e-commerce platform.  
Built with **Spring Boot**, **Kafka**, **Spring Security**, **Spring Cloud Config**, and **MySQL**.

---

## ✨ Features

- ✅ Create, view, update, and delete orders
- ✅ Role-based access control using **JWT authentication**
- ✅ Integration with **Apache Kafka** (event-driven architecture)
- ✅ Externalized configuration via **Spring Cloud Config**
- ✅ Database persistence with **Spring Data JPA** and **MySQL**
- ✅ API documentation using **Swagger**
- ✅ Unit and integration testing with **JUnit 5** and **Mockito**
- ✅ Clean, modular codebase with domain-driven design principles

---

## 🧱 Architecture Overview

This service follows a microservice architecture and communicates asynchronously via Kafka.  
It is designed to be stateless, secure, and easily deployable via Docker.

**Layers:**
- `Controller` – REST API endpoints
- `Service` – Business logic
- `Repository` – Data persistence
- `DTOs` – Data transfer between layers
- `Security` – JWT filter, user roles, and permissions
- `Kafka` – Producers and consumers for event messaging
- `Config` – Externalized configs via Spring Cloud Config

---

## 🔐 Security

The service uses **JWT-based authentication** with `ROLE_USER` and `ROLE_ADMIN`.

| Endpoint                      | Access Level     |
|------------------------------|------------------|
| `POST /orders`               | Authenticated users |
| `GET /orders/{id}`           | Owner or Admin   |
| `GET /orders/user/{userId}`  | Owner or Admin   |
| `PATCH /orders/{id}/status`  | Admin only       |
| `DELETE /orders/{id}`        | Admin only       |

---

## 📡 Kafka Integration

The service is both a **producer** and a **consumer** in Kafka.

### 📤 Produces:
- `order-created` — triggered when a new order is placed
- `order-status-changed` — triggered on status updates

### 📥 Consumes:
- `payment-confirmed` — used to mark the order as paid

---

## 🧪 Testing

- **Unit tests** for service and repository layers using JUnit & Mockito
- **Integration tests** for Kafka consumers and REST controllers
- Mocks for security context and external dependencies

---

## 📚 Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **Spring Web**
- **Spring Data JPA**
- **Spring Cloud Config**
- **Apache Kafka**
- **MySQL**
- **Swagger / OpenAPI**
- **JUnit 5 + Mockito**

---

## 📂 Project Structure

