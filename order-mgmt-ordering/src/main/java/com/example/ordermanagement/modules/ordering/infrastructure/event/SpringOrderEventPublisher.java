package com.example.ordermanagement.modules.ordering.infrastructure.event;

import com.example.ordermanagement.common.event.OrderEvent;
import com.example.ordermanagement.modules.ordering.domain.event.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpringOrderEventPublisher implements OrderEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(OrderEvent event) {
        log.info("Publishing domain event: {}", event);
        applicationEventPublisher.publishEvent(event);
    }
}