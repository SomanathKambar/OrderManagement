package com.example.ordermanagement.modules.ordering.domain.model;


import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.OrderType;
import com.example.ordermanagement.modules.ordering.domain.PaymentStatus;
import com.example.ordermanagement.common.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long  id;
    private String customerId;
    private String customerName;
    private String restaurantId;
    private String restaurantName;

    private OrderType orderType;
    private OrderStatus status;
    private PaymentStatus paymentStatus;

    private Address deliveryAddress;
    private Address restaurantAddress;

    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    private BigDecimal totalAmount;
    private BigDecimal deliveryCharge;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;

    private Long deliveryPartnerId;
    private Integer estimatedDeliveryMinutes;
    private Integer actualDeliveryMinutes;

    private String specialInstructions;
    private String cancellationReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime pickedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;

    @Deprecated
    public boolean canBeCancelled() {
        return Set.of(OrderStatus.INITIATED, OrderStatus.PENDING_PAYMENT, OrderStatus.PAID, OrderStatus.CONFIRMED, OrderStatus.PREPARING)
                .contains(status);
    }

    @Deprecated
    public boolean canBeAssigned() {
        return status == OrderStatus.CONFIRMED || status == OrderStatus.PREPARING;
    }

    public void calculateGrandTotal() {
        BigDecimal itemsTotal = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAmount = itemsTotal;
        this.grandTotal = itemsTotal
                .add(deliveryCharge != null ? deliveryCharge : BigDecimal.ZERO)
                .add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
    }

    public void assignToDeliveryPartner(Long partnerId) {
        // Assignment only allowed when order is ready for pickup
        if (status != OrderStatus.READY_FOR_PICKUP) {
            throw new IllegalStateException("Order cannot be assigned in current state: " + status + ". Must be READY_FOR_PICKUP.");
        }
        this.deliveryPartnerId = partnerId;
        this.assignedAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus newStatus) {
        if (this.status == newStatus) {
            return;
        }
        validateStatusTransition(newStatus);
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();

        switch (newStatus) {
            case PICKED_UP:
                this.pickedAt = LocalDateTime.now();
                break;
            case DELIVERED:
                this.deliveredAt = LocalDateTime.now();
                break;
            case CANCELLED:
                this.cancelledAt = LocalDateTime.now();
                break;
        }
    }

    public void cancel(String reason) {
        updateStatus(OrderStatus.CANCELLED);
        this.cancellationReason = reason;
    }

    private void validateStatusTransition(OrderStatus newStatus) {
        boolean isValid = false;
        switch (this.status) {
            case INITIATED:
                isValid = (newStatus == OrderStatus.PENDING_PAYMENT);
                break;
            case PENDING_PAYMENT:
                isValid = Set.of(OrderStatus.PAID, OrderStatus.FAILED, OrderStatus.CANCELLED).contains(newStatus);
                break;
            case PAID:
                isValid = Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED).contains(newStatus);
                break;
            case CONFIRMED:
                isValid = Set.of(OrderStatus.PREPARING, OrderStatus.CANCELLED).contains(newStatus);
                break;
            case PREPARING:
                isValid = (newStatus == OrderStatus.READY_FOR_PICKUP);
                break;
            case READY_FOR_PICKUP:
                isValid = (newStatus == OrderStatus.PICKED_UP);
                break;
            case PICKED_UP:
                isValid = (newStatus == OrderStatus.IN_TRANSIT);
                break;
            case IN_TRANSIT:
                isValid = Set.of(OrderStatus.DELIVERED, OrderStatus.FAILED).contains(newStatus);
                break;
            case FAILED:
                isValid = (newStatus == OrderStatus.REFUNDED);
                break;
            case CANCELLED:
                isValid = (newStatus == OrderStatus.REFUNDED);
                break;
            case DELIVERED:
            case REFUNDED:
                isValid = false; // Terminal states
                break;
        }

        if (!isValid) {
            throw new IllegalStateException("Invalid transition from " + this.status + " to " + newStatus);
        }
    }
}