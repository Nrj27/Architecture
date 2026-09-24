package com.example.orderengine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OrderPayloadTest {

    @Test
    void trimsAndRetainsValidPayload() {
        var payload = new OrderPayload(" order-1 ", " keyboard ", 2);

        assertEquals("order-1", payload.orderId());
        assertEquals("keyboard", payload.item());
    }

    @Test
    void acceptsBoundaryValidValues() {
        var payload = new OrderPayload("order-999", "item", 1000);

        assertEquals("order-999", payload.orderId());
        assertEquals("item", payload.item());
        assertEquals(1000, payload.quantity());
    }

    @Test
    void rejectsInvalidQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPayload("order-1", "keyboard", 0));

        assertThrows(IllegalArgumentException.class,
                () -> new OrderPayload("order-1", "keyboard", 1001));
    }

    @Test
    void rejectsBlankItem() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPayload("order-1", "   ", 1));
    }

    @Test
    void rejectsBlankOrderId() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPayload("   ", "keyboard", 1));
    }

    @Test
    void rejectsNullItem() {
        assertThrows(NullPointerException.class,
                () -> new OrderPayload("order-1", null, 1));
    }

    @Test
    void rejectsNullOrderId() {
        assertThrows(NullPointerException.class,
                () -> new OrderPayload(null, "keyboard", 1));
    }

    @Test
    void allowsLongButValidValues() {
        var validItem = "x".repeat(256);
        assertDoesNotThrow(() -> new OrderPayload("order-1", validItem, 1));
    }

    @Test
    void rejectsOversizedItem() {
        var oversizedItem = "x".repeat(257);
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPayload("order-1", oversizedItem, 1));
    }

    @Test
    void rejectsOversizedOrderId() {
        var oversizedOrderId = "x".repeat(65);
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPayload(oversizedOrderId, "keyboard", 1));
    }
}
