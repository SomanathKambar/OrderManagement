package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderCreatedEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId,
    String customerId,
    String restaurantId,
    Double amount
) implements OrderEvent {}