package com.example.ordermanagement.modules.ordering.domain;

public enum OrderStatus {
    INITIATED,
    PENDING_PAYMENT,
    PAID,
    CONFIRMED,
    PREPARING,
    READY_FOR_PICKUP,
    PICKED_UP,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED,
    FAILED,
    REFUNDED
}
