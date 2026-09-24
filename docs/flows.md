# Runtime Flows

## Request flow

```mermaid
sequenceDiagram
    autonumber
    participant C as Client
    participant G as API Gateway
    participant A as Auth Service
    participant S as Application Service
    participant D as Domain Layer
    participant DB as Database
    participant T as Telemetry

    C->>G: HTTPS request with access token
    G->>A: Validate token and required scope
    A-->>G: Identity and authorization result
    G->>S: Forward authorized request
    S->>D: Execute use case
    D->>DB: Read or write transaction data
    DB-->>D: Transaction result
    D-->>S: Use-case result and domain events
    S->>T: Emit logs, metrics, and trace span
    S-->>G: API response
    G-->>C: HTTPS response
```

## Event flow

```mermaid
sequenceDiagram
    autonumber
    participant D as Domain Layer
    participant B as Message Broker
    participant W as Worker
    participant Q as Retry Queue
    participant X as Search / External API
    participant T as Telemetry

    D->>B: Publish domain event
    B->>W: Deliver event
    W->>X: Execute side effect
    X-->>W: Success
    W->>T: Record processing result
    Note over W,Q: On failure, retry with backoff
    W->>Q: Enqueue failed event
    Q->>W: Redeliver event
```

## Failure flow

```mermaid
flowchart TD
    Request[Incoming request] --> Timeout{Dependency responds before timeout?}
    Timeout -->|Yes| Success[Continue request]
    Timeout -->|No| Breaker[Circuit breaker records failure]
    Breaker --> Retryable{Safe to retry?}
    Retryable -->|Yes| Retry[Bounded exponential backoff]
    Retry --> Timeout
    Retryable -->|No| Fallback[Return safe fallback or error]
    Event[Background event] --> Consumer[Consumer]
    Consumer --> Processed{Processed successfully?}
    Processed -->|Yes| Ack[Acknowledge event]
    Processed -->|No| Attempts{Retry limit reached?}
    Attempts -->|No| Delayed[Delay and retry]
    Delayed --> Consumer
    Attempts -->|Yes| DLQ[Dead-letter queue and alert]
```

## Deployment flow

1. A change is submitted through a pull request.
2. Automated checks validate Markdown, Mermaid syntax where supported, security policy, and documentation links.
3. A reviewed change is merged into the default branch.
4. The deployment system builds immutable artifacts and scans dependencies.
5. The release is deployed progressively, with health checks and rollback criteria.
6. Dashboards and alerts verify service-level objectives after deployment.

## Operational checklist

- [ ] Health and readiness checks exist for each deployable service.
- [ ] Timeouts are configured for every network dependency.
- [ ] Retries are bounded and safe for the operation.
- [ ] Consumers are idempotent.
- [ ] Queue depth and dead-letter volume are monitored.
- [ ] Backups are tested, not only configured.
- [ ] Runbooks exist for common alerts.
- [ ] Logs exclude credentials and sensitive personal data.
