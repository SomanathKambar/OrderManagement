package com.example.ordermanagement.infrastructure.kafka;

import com.example.ordermanagement.modules.ordering.infrastructure.outbox.entity.OutboxEvent;
import com.example.ordermanagement.modules.ordering.infrastructure.outbox.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxMessageRelay {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final int MAX_RETRIES = 5;

    @Scheduled(fixedDelay = 2000) // Poll every 2 seconds
    @Transactional
    public void processOutbox() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatusOrderByCreatedAtAsc(OutboxEvent.OutboxStatus.PENDING);
        
        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Processing {} pending outbox events", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                // Send to Kafka synchronously to ensure we catch errors here
                kafkaTemplate.send(event.getTopic(), event.getAggregateId(), event.getPayload()).get();

                event.setStatus(OutboxEvent.OutboxStatus.PUBLISHED);
                event.setProcessedAt(LocalDateTime.now());
                log.debug("Successfully published event: {}", event.getId());
                
            } catch (Exception e) {
                log.error("Error processing outbox event: {}. Retry count: {}", event.getId(), event.getRetryCount(), e);
                event.setRetryCount(event.getRetryCount() + 1);
                
                if (event.getRetryCount() >= MAX_RETRIES) {
                    event.setStatus(OutboxEvent.OutboxStatus.FAILED);
                    log.error("Event {} failed after {} retries", event.getId(), MAX_RETRIES);
                }
                // Leave as PENDING for next poll if retryCount < MAX_RETRIES
            }
        }
        
        outboxEventRepository.saveAll(pendingEvents);
    }
}
