# Microservices Demo (Java 8 + Spring Boot 2.7 + Docker + REST)

This repo contains a small, production-style microservices playground to showcase distributed system basics with containerization.

**Services**
- `user-service` (8081): Manage users.
- `order-service` (8082): Create orders for a user (calls user-service).
- `payment-service` (8083): Capture (mock) payments for an order (calls order-service).

**Tech**
- Java 8, Spring Boot 2.7.18
- REST (WebClient for service-to-service calls)
- PostgreSQL (user-service, order-service)
- Docker & Docker Compose
- Swagger UI via springdoc-openapi

> Optional: You can extend this to event-driven communication by adding Kafka (not included in this scaffold).

---

## Architecture

```
client ──> user-service (DB: userdb)

client ──> order-service (DB: orderdb) ──(REST)──> user-service

client ──> payment-service ──(REST)──> order-service
```

Each service has its own Dockerfile. `docker-compose.yml` wires services together on the same network with stable DNS names.

---

## Getting Started

**Prerequisites**
- Java 8
- Maven 3.8+
- Docker Desktop (or Docker Engine) + Docker Compose

**Build**
```bash
mvn -q -DskipTests package --file user-service/pom.xml
mvn -q -DskipTests package --file order-service/pom.xml
mvn -q -DskipTests package --file payment-service/pom.xml
```

**Run**
```bash
docker compose up --build
```

**Smoke Test**
```bash
# Create a user
curl -X POST localhost:8081/api/users   -H 'Content-Type: application/json'   -d '{"email":"alice@example.com","fullName":"Alice"}'

# Create an order for userId=1
curl -X POST "localhost:8082/api/orders?userId=1&amount=49.99"

# Pay the order (orderId=1)
curl -X POST "localhost:8083/api/payments?orderId=1"
```

**Swagger UIs**
- http://localhost:8081/swagger-ui.html
- http://localhost:8082/swagger-ui.html
- http://localhost:8083/swagger-ui.html

---

## Notes & Next Steps

- Add Flyway migrations (replace `ddl-auto=update`).
- Introduce timeouts/retries with `WebClient` for resilience.
- Add Testcontainers-based integration tests.
- Add GitHub Actions for PR build/test and Docker image publishing.
- Add a Config Server + centralized config and OpenTelemetry tracing.