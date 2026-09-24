# reactive-order-engine

This project is a Java 21 asynchronous order intake engine using Spring Boot 3.4.3 and Vert.x 4.5.13. It accepts validated orders, returns `202 Accepted`, and publishes events to Vert.x Event Bus consumers that process inventory and notification work on Java 21 virtual threads.

## Requirements

- JDK 21+
- Gradle 8.10+ or a generated Gradle wrapper

## Run

```bash
./gradlew bootRun
```

On Windows:

```powershell
gradlew.bat bootRun
```

## Submit an order

```bash
curl -i -X POST http://localhost:8080/api/v1/orders \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"order-1001","item":"mechanical keyboard","quantity":2}'
```

A valid request returns `202 Accepted` immediately:

```json
{"status":"accepted","orderId":"order-1001"}
```

The event is then delivered to the inventory and notification consumers asynchronously.

## Security controls

- Request bodies are limited to 10 KB.
- `orderId`, `item`, and `quantity` are validated at the boundary.
- Security response headers are added to every route.
- Application processing is asynchronous and does not block the HTTP event loop.
- Consumers run on bounded-by-workload virtual-thread executors and log failures.
- No credentials or external integrations are hard-coded.

> Before production deployment, terminate TLS at a trusted edge, configure a real identity provider, add authentication and authorization middleware, use a durable broker, add persistence, configure secret management, and run organization-approved SAST, DAST, SCA, and penetration tests. This sample demonstrates hardened boundaries but cannot itself certify a VAPT result.

## Project layout

```text
src/main/java/com/example/orderengine/
├── InventoryService.java
├── InventoryVerticle.java
├── NotificationService.java
├── NotificationVerticle.java
├── OrderEngineApplication.java
├── OrderEvent.java
├── OrderHttpVerticle.java
├── OrderPayload.java
└── VertxInitializer.java
```

See [`docs/commit-sequence.md`](docs/commit-sequence.md) for the requested 15-minute commit simulation and [`docs/architecture-overview.md`](docs/architecture-overview.md) for the broader system design.
