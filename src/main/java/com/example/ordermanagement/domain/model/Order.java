package com.example.ordermanagement.domain.model;


import com.example.ordermanagement.domain.enums.OrderStatus;
import com.example.ordermanagement.domain.enums.OrderType;
import com.example.ordermanagement.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String specialInstructions; // Added field
    private String cancellationReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime pickedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;

    public boolean canBeCancelled() {
        return status == OrderStatus.CREATED || status == OrderStatus.ASSIGNED;
    }

    public boolean canBeAssigned() {
        return status == OrderStatus.CREATED;
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
        if (!canBeAssigned()) {
            throw new IllegalStateException("Order cannot be assigned in current state: " + status);
        }
        this.deliveryPartnerId = partnerId;
        this.status = OrderStatus.ASSIGNED;
        this.assignedAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus newStatus) {
        validateStatusTransition(newStatus);
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();

        switch (newStatus) {
            case PICKED:
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
        if (!canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in current state: " + status);
        }
        this.status = OrderStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancelledAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateStatusTransition(OrderStatus newStatus) {
        if (this.status == OrderStatus.CANCELLED && newStatus != OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot transition from CANCELLED to " + newStatus);
        }
        if (this.status == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot transition from DELIVERED to " + newStatus);
        }
    }
}