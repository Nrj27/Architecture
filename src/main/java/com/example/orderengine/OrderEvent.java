package com.example.orderengine;

import java.time.Instant;

/** Domain events exchanged through the local Vert.x event bus. */
public sealed interface OrderEvent permits OrderEvent.Created, OrderEvent.Cancelled {

    String orderId();

    Instant occurredAt();

    record Created(OrderPayload order, Instant occurredAt) implements OrderEvent {
        public Created {
            if (order == null || occurredAt == null) {
                throw new IllegalArgumentException("order and occurredAt are required");
            }
        }

        @Override
        public String orderId() {
            return order.orderId();
        }
    }

    record Cancelled(String orderId, Instant occurredAt) implements OrderEvent {
        public Cancelled {
            if (orderId == null || orderId.isBlank() || occurredAt == null) {
                throw new IllegalArgumentException("orderId and occurredAt are required");
            }
        }
    }
}
