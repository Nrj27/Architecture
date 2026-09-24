package com.example.orderengine;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(InventoryVerticle.class);
    private final InventoryService inventoryService;
    private ExecutorService virtualThreads;

    public InventoryVerticle(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        virtualThreads = Executors.newVirtualThreadPerTaskExecutor();
        vertx.eventBus().consumer("order.created", message -> virtualThreads.submit(() -> {
            try {
                inventoryService.process((OrderEvent) message.body());
            } catch (RuntimeException exception) {
                log.error("Inventory processing failed", exception);
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
