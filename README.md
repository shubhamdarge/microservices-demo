# Microservices Demo (Java + Spring Boot + REST)

A small, student-friendly project to practice **microservices** concepts with Java and Spring Boot.  
It models a fake shop with **users**, **orders**, and **payments** as separate services.

---

## What this is

- **Three services**, each focused on one thing:
  - **user-service** (port 8081) — create & read users
  - **order-service** (8082) — create orders for a user; stores status
  - **payment-service** (8083) — “charges” an order by calling order-service

- **Databases**: each service has its own DB (PostgreSQL).  
  This demonstrates the **database-per-service** pattern.

- **Service-to-service calls**: done with **WebClient** (from `spring-boot-starter-webflux`).

- **API docs**: each service exposes Swagger UI.

- **Tech stack**: Java 8, Spring Boot 2.7.18, Spring Web (MVC) + WebFlux (WebClient), Spring Data JPA, PostgreSQL, Lombok, springdoc-openapi.