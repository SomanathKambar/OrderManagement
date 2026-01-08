package com.example.ordermanagement.modules.ordering.api;

import com.example.ordermanagement.common.event.OrderEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Real-time", description = "Order real-time updates")
@Slf4j
public class OrderStreamController {

    // Map<OrderId, SseEmitter> - simplified for demo. 
    // In production, use Redis Pub/Sub for multi-instance support.
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Stream order updates (SSE)")
    public SseEmitter streamOrderUpdates(@PathVariable String id) {
        log.info("New SSE connection for order: {}", id);
        
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Keep alive
        emitters.put(id, emitter);

        emitter.onCompletion(() -> {
            log.info("SSE completed for order: {}", id);
            emitters.remove(id);
        });
        
        emitter.onTimeout(() -> {
            log.info("SSE timeout for order: {}", id);
            emitter.complete();
            emitters.remove(id);
        });

        // Send initial connection confirmation
        try {
            emitter.send(SseEmitter.event().name("connected").data("Connected to order stream: " + id));
        } catch (IOException e) {
            log.warn("Failed to send initial SSE event", e);
        }

        return emitter;
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        String orderId = String.valueOf(event.orderId());
        SseEmitter emitter = emitters.get(orderId);
        
        if (emitter != null) {
            try {
                log.info("Pushing event {} to SSE client for order {}", event.getClass().getSimpleName(), orderId);
                // We send the event type as the event name, and the event itself as data
                emitter.send(SseEmitter.event()
                        .name("status-update")
                        .data(event)); 
            } catch (Exception e) {
                log.warn("Failed to push SSE, removing emitter for order {}", orderId);
                emitters.remove(orderId);
            }
        }
    }
}
