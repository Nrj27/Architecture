# reactive-order-engine

A production-ready reference implementation of an asynchronous order processing engine built with Java 21, Spring Boot 3.4.3, and Vert.x 4.5.13.

This project demonstrates secure request handling, Java 21 virtual-thread execution, event-driven processing, and VAPT-oriented defensive coding practices.

> Note: this is a hardened reference implementation for architecture and engineering demonstration. It is not a formal VAPT certification or a production deployment blueprint by itself. Production deployments still require identity validation, TLS, secrets management, durable messaging infrastructure, and independent security testing.

## Stack

- Java 21
- Spring Boot 3.4.3
- Vert.x 4.5.13
- Gradle Kotlin DSL
- JUnit 5

## Features

- Asynchronous HTTP order intake on port 8080
- 202 Accepted response for accepted orders
- Event bus communication on `order.created`
- Inventory and notification consumers
- Java 21 virtual-thread executors
- Validation for `orderId`, `item`, and `quantity`
- 10 KB request-body cap
- Security headers for hardening

## Run

```bash
gradle bootRun
```

Or, if using the included launcher:

```bash
./gradlew bootRun
```

## Example request

```bash
curl -i -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"orderId":"order-1001","item":"mechanical keyboard","quantity":2}'
```

Expected response:

```json
{"status":"accepted","orderId":"order-1001"}
```

## Security controls

- Input validation for `orderId`, `item`, and `quantity`
- Request body size capped at 10 KB
- Security headers added to all responses
- Event-driven side effects operate asynchronously from the request path
- Java 21 virtual threads are used for worker execution

## Project structure

```text
src/main/java/com/example/orderengine/
├── OrderEngineApplication.java
├── OrderPayload.java
├── OrderEvent.java
├── OrderHttpVerticle.java
├── InventoryService.java
├── InventoryVerticle.java
├── NotificationService.java
├── NotificationVerticle.java
├── VertxInitializer.java
└── src/test/java/com/example/orderengine/OrderPayloadTest.java
```

## Commit sequence simulation

See [`docs/commit-sequence.md`](docs/commit-sequence.md) for a 15-minute step-by-step commit plan.

## LinkedIn caption

I’m excited to share a new project: reactive-order-engine — a Java 21, Spring Boot 3.4.3, and Vert.x 4.5.13 asynchronous order-processing engine designed with security hardening and event-driven architecture in mind.

This project demonstrates non-blocking HTTP intake, immediate 202 Accepted responses, virtual-thread-based workers, strict input validation, and defensive security headers.

GitHub: https://github.com/Nrj27/Architecture

#Java #SpringBoot #Vertx #Java21 #BackendDevelopment #SoftwareArchitecture #SystemDesign #AsyncProgramming #Engineering
