package com.example.ordermanagement.modules.ordering.infrastructure.event;

import com.example.ordermanagement.common.event.OrderEvent;
import com.example.ordermanagement.modules.ordering.domain.event.OrderEventPublisher;
import com.example.ordermanagement.modules.ordering.infrastructure.outbox.entity.OutboxEvent;
import com.example.ordermanagement.modules.ordering.infrastructure.outbox.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpringOrderEventPublisher implements OrderEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(OrderEvent event) {
        log.info("Persisting domain event to Outbox: {}", event.getClass().getSimpleName());
        
        try {
            String payload = objectMapper.writeValueAsString(event);
            
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateType("ORDER")
                    .aggregateId(event.getOrderId() != null ? event.getOrderId().toString() : "UNKNOWN")
                    .eventType(event.getClass().getSimpleName())
                    .topic("orders.events")
                    .payload(payload)
                    .status(OutboxEvent.OutboxStatus.PENDING)
                    .build();
            
            outboxEventRepository.save(outboxEvent);
            
            // Still publish internally for any synchronous side-effects (logging, etc)
            applicationEventPublisher.publishEvent(event);
            
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event for Outbox", e);
            throw new RuntimeException("Failed to persist event", e);
        }
    }
}