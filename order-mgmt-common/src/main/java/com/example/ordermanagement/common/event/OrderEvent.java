package com.example.ordermanagement.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class OrderEvent {
    private String eventId;
    private LocalDateTime occurredAt;
    private Long orderId;
}
