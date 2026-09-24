package com.example.orderengine;

import java.util.Objects;
import java.util.Optional;

/** Immutable and validated order input accepted by the HTTP boundary. */
public record OrderPayload(String orderId, String item, int quantity) {

    private static final int MAX_ORDER_ID_LENGTH = 64;
    private static final int MAX_ITEM_LENGTH = 256;
    private static final int MAX_QUANTITY = 1_000;

    public OrderPayload {
        orderId = Optional.ofNullable(orderId).map(String::trim).orElse("");
        item = Optional.ofNullable(item).map(String::trim).orElse("");

        if (orderId.isBlank() || orderId.length() > MAX_ORDER_ID_LENGTH) {
            throw new IllegalArgumentException("orderId must be present and at most 64 characters");
        }
        if (item.isBlank() || item.length() > MAX_ITEM_LENGTH) {
            throw new IllegalArgumentException("item must be present and at most 256 characters");
        }
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("quantity must be between 1 and 1000");
        }
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(item);
    }
}
