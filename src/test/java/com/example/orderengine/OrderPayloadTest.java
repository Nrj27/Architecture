package com.example.orderengine;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderHttpVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(OrderHttpVerticle.class);
    private static final int PORT = 8080;
    private static final long MAX_BODY_BYTES = 10 * 1024;
    private HttpServer server;

    @Override
    public void start(Promise<Void> startPromise) {
        var router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setBodyLimit(MAX_BODY_BYTES));
        router.route().handler(this::securityHeaders);
        router.post("/api/v1/orders").handler(this::createOrder);
        router.get("/health").handler(context -> context.response().end("UP"));

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(PORT)
                .onSuccess(httpServer -> {
                    server = httpServer;
                    log.info("Order HTTP API listening on port {}", PORT);
                    startPromise.complete();
                })
                .onFailure(startPromise::fail);
    }

    private void securityHeaders(RoutingContext context) {
        var headers = context.response().headers();
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("X-Frame-Options", "DENY");
        headers.set("X-XSS-Protection", "0");
        headers.set("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        context.next();
    }

    private void createOrder(RoutingContext context) {
        try {
            var body = context.body().asJsonObject();
            if (body == null) {
                throw new IllegalArgumentException("A JSON request body is required");
            }

            var payload = new OrderPayload(
                    body.getString("orderId"),
                    body.getString("item"),
                    body.getInteger("quantity", 0)
            );

            var event = new OrderEvent.Created(payload, Instant.now());
            vertx.eventBus().publish("order.created", event);

            var response = new JsonObject()
                    .put("status", "accepted")
                    .put("orderId", payload.orderId());

            context.response()
                    .setStatusCode(202)
                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .end(response.encode());
        } catch (IllegalArgumentException | ClassCastException exception) {
            context.response().setStatusCode(400)
                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .end(new JsonObject().put("error", exception.getMessage()).encode());
        } catch (RuntimeException exception) {
            log.error("Unexpected order submission failure", exception);
            context.response().setStatusCode(500).end();
        }
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        if (server == null) {
            stopPromise.complete();
        } else {
            server.close().onComplete(ignored -> stopPromise.complete());
        }
    }
}
