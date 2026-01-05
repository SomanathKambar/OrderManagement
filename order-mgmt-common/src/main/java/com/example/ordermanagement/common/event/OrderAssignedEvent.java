package com.example.ordermanagement.common.event;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderAssignedEvent(
    String eventId,
    LocalDateTime occurredAt,
    Long orderId,
    Long deliveryPartnerId
) implements OrderEvent {}