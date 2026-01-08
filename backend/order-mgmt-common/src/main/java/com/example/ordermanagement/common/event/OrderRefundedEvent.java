package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderRefundedEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId,
    Double amount
) implements OrderEvent {}