package com.example.orderengine;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void process(OrderEvent event) {
        var notification = switch (event) {
            case OrderEvent.Created created -> createdNotification(created);
            case OrderEvent.Cancelled cancelled -> cancelledNotification(cancelled);
        };
        log.info("Notification event processed: {}", notification);
    }

    private String createdNotification(OrderEvent.Created created) {
        var channels = Stream.of("email", "audit")
                .filter(channel -> !channel.isBlank())
                .collect(Collectors.joining(", "));
        return "queued " + channels + " notification for order " + created.orderId();
    }

    private String cancelledNotification(OrderEvent.Cancelled cancelled) {
        var reason = Optional.of(cancelled.orderId())
                .filter(value -> !value.isBlank())
                .map(value -> "cancellation of " + value)
                .orElse("unknown cancellation");
        return "queued notification for " + reason;
    }
}
