package com.example.ordermanagement.infrastructure.kafka;

import com.example.ordermanagement.common.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "orders.events";

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(OrderEvent event) {
        log.info("Forwarding event to Kafka: {}", event);
        try {
            // Use orderId as key to ensure ordering per order
            String key = event.getOrderId() != null ? event.getOrderId().toString() : event.getEventId();
            
            kafkaTemplate.send(TOPIC, key, event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.debug("Event {} sent to topic {} partition {}", 
                                    event.getEventId(), TOPIC, result.getRecordMetadata().partition());
                        } else {
                            log.error("Failed to send event {} to Kafka", event.getEventId(), ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending event to Kafka", e);
            // In a real production system, we might want to save this to a DLQ or retry table here
            // since the DB transaction is already committed.
        }
    }
}
