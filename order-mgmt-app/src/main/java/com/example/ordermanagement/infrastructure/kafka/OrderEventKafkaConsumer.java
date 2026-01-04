package com.example.ordermanagement.infrastructure.kafka;

import com.example.ordermanagement.common.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventKafkaConsumer {

    @KafkaListener(topics = "orders.events", groupId = "order-management-group")
    public void consume(OrderEvent event) {
        log.info("Received Kafka Event: Type={}, ID={}, OrderID={}", 
                event.getClass().getSimpleName(), event.getEventId(), event.getOrderId());
    }
}
