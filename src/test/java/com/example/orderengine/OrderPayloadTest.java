package com.example.orderengine;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OrderPayloadTest {

    @Test
    void trimsAndRetainsValidPayload() {
        var payload = new OrderPayload(" order-1 ", " keyboard ", 2);

        assertEquals("order-1", payload.orderId());
        assertEquals("keyboard", payload.item());
    }

    @Test
    void rejectsInvalidQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPayload("order-1", "keyboard", 0));
    }

    @Test
    void rejectsBlankItem() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPayload("order-1", "   ", 1));
    }
}
