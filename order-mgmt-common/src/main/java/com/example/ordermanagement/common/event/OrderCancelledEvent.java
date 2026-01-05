package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderCancelledEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId,
    String reason
) implements OrderEvent {}