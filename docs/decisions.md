# Architecture Decision Records

## ADR-001: Separate synchronous and asynchronous workloads

- **Status:** Accepted
- **Decision:** Keep user-facing request processing separate from long-running or retryable work. Publish domain events to a broker and process them with workers.
- **Why:** This prevents slow side effects from increasing API latency and allows workers to scale independently.
- **Trade-offs:** Adds broker, worker, retry, and observability complexity. Results may become eventually consistent.

## ADR-002: Keep the application tier stateless

- **Status:** Accepted
- **Decision:** Do not store user session state in an individual application instance. Use signed tokens or a shared session store when server-side sessions are required.
- **Why:** Stateless instances are easier to replace, autoscale, and deploy progressively.
- **Trade-offs:** Shared state introduces its own availability and consistency concerns; tokens require careful expiration and revocation design.

## ADR-003: Treat the cache as an optimization

- **Status:** Accepted
- **Decision:** The database remains the source of truth. Cache misses and cache failures must have a safe database path for operations where latency permits.
- **Why:** Correctness should not depend on an optimization layer.
- **Trade-offs:** Database capacity and query performance still need to support cache misses and invalidations.

## ADR-004: Use at-least-once event handling with idempotent consumers

- **Status:** Accepted
- **Decision:** Consumers assume an event can be delivered more than once and use an idempotency key or processed-event record.
- **Why:** At-least-once delivery is generally more resilient than losing work silently.
- **Trade-offs:** Consumers need deduplication logic and durable processing state.

## ADR-005: Make observability a platform concern

- **Status:** Accepted
- **Decision:** Standardize correlation IDs, structured logs, core metrics, and distributed tracing across gateway, application, and worker components.
- **Why:** Cross-component failures cannot be diagnosed reliably from isolated logs.
- **Trade-offs:** Instrumentation adds implementation effort and telemetry storage cost.

## Template for future decisions

```markdown
## ADR-NNN: Decision title

- **Status:** Proposed | Accepted | Superseded | Deprecated
- **Decision:** What was decided?
- **Why:** What problem does this solve?
- **Alternatives:** What other options were considered?
- **Trade-offs:** What costs or risks are accepted?
```
