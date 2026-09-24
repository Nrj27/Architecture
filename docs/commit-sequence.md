# Commit Sequence Simulation

The following is a suggested history for building `reactive-order-engine`. The timestamps are illustrative and intentionally separated by 15 minutes.

| Time | Commit message | Scope |
| --- | --- | --- |
| 09:00 | `chore: initialize Java 21 Gradle Kotlin project` | Gradle Kotlin DSL, Java toolchain, Spring Boot, Vert.x dependencies |
| 09:15 | `feat: add validated order payload and domain events` | `OrderPayload`, sealed `OrderEvent`, validation tests |
| 09:30 | `feat: add asynchronous inventory and notification services` | Spring services, functional processing, Java 21 pattern matching |
| 09:45 | `feat: add Vert.x HTTP order intake` | Port 8080 API, 202 response, JSON handling, 10 KB body limit |
| 10:00 | `feat: add virtual-thread event consumers` | Inventory and notification Verticles with virtual-thread executors |
| 10:15 | `feat: wire Vert.x lifecycle and security headers` | Spring initializer, graceful shutdown, response hardening |
| 10:30 | `docs: add architecture and operational guidance` | README, flows, ADRs, contribution guide, license |
| 10:45 | `test: cover order payload validation` | Unit coverage for normalization and boundary validation |

To reproduce the sequence locally, make one focused change per commit and wait 15 minutes between commits. The repository itself was delivered as one atomic update for convenience.
