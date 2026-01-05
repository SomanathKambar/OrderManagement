package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderCompletedEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId
) implements OrderEvent {}