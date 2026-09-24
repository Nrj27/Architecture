# reactive-order-engine

A Java 21 asynchronous order-processing engine using Spring Boot 3.4.3, Vert.x 4.5.13, and Gradle Kotlin DSL.

This reference implementation demonstrates non-blocking HTTP intake, event-driven processing, virtual-thread workers, boundary validation, a 10 KB body limit, and security response headers.

> This is a VAPT-oriented reference implementation, not a formal VAPT certification. Production use requires authentication, authorization, TLS, durable messaging, persistence, idempotency, secrets management, monitoring, and independent security testing.

## Run

Requirements: JDK 21+ and Gradle 8.10+.

```bash
gradle bootRun
gradle clean test
```

The included `gradlew` and `gradlew.bat` files are convenience launchers that delegate to an installed Gradle executable; they are not a full Gradle Wrapper distribution.

## Example request

```bash
curl -i -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"orderId":"order-1001","item":"mechanical keyboard","quantity":2}'
```

A valid request returns `202 Accepted` and publishes to `order.created`.

## Security controls

- Input validation for `orderId`, `item`, and `quantity`.
- Request body size capped at 10 KB.
- Required security headers added to responses.
- Inventory and notification consumers use Java 21 virtual threads.

## Project layout

The Gradle project is intentionally located at the repository root:

```text
build.gradle.kts
settings.gradle.kts
src/
├── main/
│   ├── java/com/example/orderengine/
│   └── resources/
└── test/
    └── java/com/example/orderengine/OrderPayloadTest.java
```

See [`../docs/commit-sequence.md`](../docs/commit-sequence.md) for the 15-minute commit simulation and the LinkedIn showcase caption.
