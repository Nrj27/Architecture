package com.example.orderengine;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(NotificationVerticle.class);
    private final NotificationService notificationService;
    private ExecutorService virtualThreads;

    public NotificationVerticle(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        virtualThreads = Executors.newVirtualThreadPerTaskExecutor();
        vertx.eventBus().consumer("order.created", message -> virtualThreads.submit(() -> {
            try {
                notificationService.process((OrderEvent) message.body());
            } catch (RuntimeException exception) {
                log.error("Notification processing failed", exception);
            }
        }));
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        if (virtualThreads != null) {
            virtualThreads.close();
        }
        stopPromise.complete();
    }
}
