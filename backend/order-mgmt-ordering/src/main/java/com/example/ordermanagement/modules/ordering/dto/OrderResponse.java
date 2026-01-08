package com.example.ordermanagement.modules.ordering.dto;

import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.OrderType;
import com.example.ordermanagement.modules.ordering.domain.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Schema(description = "Order response with all details")
public record OrderResponse(
    @Schema(description = "Order ID", example = "12345")
    Long id,

    @Schema(description = "Customer ID", example = "cust_12345")
    String customerId,

    @Schema(description = "Customer name", example = "John Doe")
    String customerName,

    @Schema(description = "Restaurant ID", example = "rest_67890")
    String restaurantId,

    @Schema(description = "Restaurant name", example = "Pizza Hut")
    String restaurantName,

    @Schema(description = "Current order status", example = "CREATED")
    OrderStatus status,

    @Schema(description = "Order type", example = "FOOD")
    OrderType orderType,

    @Schema(description = "Payment status", example = "PENDING")
    PaymentStatus paymentStatus,

    @Schema(description = "Delivery partner ID", example = "456")
    Long deliveryPartnerId,

    @Schema(description = "Total amount before tax and delivery", example = "299.99")
    BigDecimal totalAmount,

    @Schema(description = "Delivery charge", example = "29.99")
    BigDecimal deliveryCharge,

    @Schema(description = "Tax amount", example = "5.00")
    BigDecimal taxAmount,

    @Schema(description = "Grand total", example = "334.98")
    BigDecimal grandTotal,

    @Schema(description = "Estimated delivery minutes", example = "45")
    Integer estimatedDeliveryMinutes,

    @Schema(description = "Actual delivery minutes", example = "40")
    Integer actualDeliveryMinutes,

    @Schema(description = "Special instructions for delivery", example = "Leave at door")
    String specialInstructions,

    @Schema(description = "Cancellation reason", example = "Customer requested")
    String cancellationReason,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order creation timestamp", example = "2024-01-15T10:30:00")
    LocalDateTime createdAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order last update timestamp", example = "2024-01-15T10:30:00")
    LocalDateTime updatedAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order assigned timestamp", example = "2024-01-15T10:35:00")
    LocalDateTime assignedAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order picked timestamp", example = "2024-01-15T10:40:00")
    LocalDateTime pickedAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order delivered timestamp", example = "2024-01-15T11:10:00")
    LocalDateTime deliveredAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order cancelled timestamp", example = "2024-01-15T10:32:00")
    LocalDateTime cancelledAt,

    @Schema(description = "Delivery city", example = "Bangalore")
    String deliveryCity,

    @Schema(description = "Restaurant city", example = "Bangalore")
    String restaurantCity
) {}
