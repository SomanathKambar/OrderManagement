package com.example.ordermanagement.modules.ordering.domain.event;

import com.example.ordermanagement.common.event.OrderEvent;

public interface OrderEventPublisher {
    void publish(OrderEvent event);
}