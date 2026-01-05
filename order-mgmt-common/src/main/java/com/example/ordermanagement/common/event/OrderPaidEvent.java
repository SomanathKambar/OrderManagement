package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderPaidEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId,
    String paymentId
) implements OrderEvent {}