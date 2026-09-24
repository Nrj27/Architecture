package com.example.orderengine;

import io.vertx.core.Vertx;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VertxInitializer {

    private static final Logger log = LoggerFactory.getLogger(VertxInitializer.class);
    private final Vertx vertx;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;

    public VertxInitializer(InventoryService inventoryService, NotificationService notificationService) {
        this.vertx = Vertx.vertx();
        this.inventoryService = inventoryService;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void deployVerticles() {
        vertx.deployVerticle(new InventoryVerticle(inventoryService));
        vertx.deployVerticle(new NotificationVerticle(notificationService));
        vertx.deployVerticle(new OrderHttpVerticle());
        log.info("Vert.x order-processing verticles deployment requested");
    }

    @PreDestroy
    public void shutdown() {
        vertx.close().onFailure(error -> log.warn("Vert.x shutdown failed", error));
    }
}
