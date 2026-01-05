package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderReadyEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId
) implements OrderEvent {}