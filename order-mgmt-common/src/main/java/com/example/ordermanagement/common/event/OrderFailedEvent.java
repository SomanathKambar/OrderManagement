package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderFailedEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId,
    String reason
) implements OrderEvent {}