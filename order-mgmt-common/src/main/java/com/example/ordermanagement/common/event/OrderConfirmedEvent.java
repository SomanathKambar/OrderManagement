package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderConfirmedEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId
) implements OrderEvent {}