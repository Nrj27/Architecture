package com.example.orderengine;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    public void process(OrderEvent event) {
        var message = switch (event) {
            case OrderEvent.Created created -> reserve(created);
            case OrderEvent.Cancelled cancelled -> release(cancelled);
        };
        log.info("Inventory event processed: {}", message);
    }

    private String reserve(OrderEvent.Created created) {
        var normalizedItems = Stream.of(created.order().item())
                .map(String::trim)
                .map(value -> value.toLowerCase(Locale.ROOT))
                .filter(value -> !value.isBlank())
                .collect(Collectors.toUnmodifiableList());
        var quantity = created.order().quantity();
        return "reserved " + quantity + " unit(s) of " + String.join(", ", normalizedItems)
                + " for order " + created.orderId();
    }

    private String release(OrderEvent.Cancelled cancelled) {
        return "released inventory for cancelled order " + cancelled.orderId();
    }
}
