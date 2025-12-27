package com.example.ordermanagement.controller.dto.response;

import com.example.ordermanagement.domain.enums.OrderStatus;
import com.example.ordermanagement.domain.enums.OrderType;
import com.example.ordermanagement.domain.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order response with all details")
public class OrderResponse {

    @Schema(description = "Order ID", example = "12345")
    private Long id;

    @Schema(description = "Customer ID", example = "cust_12345")
    private String customerId;

    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;

    @Schema(description = "Restaurant ID", example = "rest_67890")
    private String restaurantId;

    @Schema(description = "Restaurant name", example = "Pizza Hut")
    private String restaurantName;

    @Schema(description = "Current order status", example = "CREATED")
    private OrderStatus status;

    @Schema(description = "Order type", example = "FOOD")
    private OrderType orderType;

    @Schema(description = "Payment status", example = "PENDING")
    private PaymentStatus paymentStatus;

    @Schema(description = "Delivery partner ID", example = "456")
    private Long deliveryPartnerId;

    @Schema(description = "Total amount before tax and delivery", example = "299.99")
    private BigDecimal totalAmount;

    @Schema(description = "Delivery charge", example = "29.99")
    private BigDecimal deliveryCharge;

    @Schema(description = "Tax amount", example = "5.00")
    private BigDecimal taxAmount;

    @Schema(description = "Grand total", example = "334.98")
    private BigDecimal grandTotal;

    @Schema(description = "Estimated delivery minutes", example = "45")
    private Integer estimatedDeliveryMinutes;

    @Schema(description = "Actual delivery minutes", example = "40")
    private Integer actualDeliveryMinutes;

    @Schema(description = "Special instructions for delivery", example = "Leave at door")
    private String specialInstructions;

    @Schema(description = "Cancellation reason", example = "Customer requested")
    private String cancellationReason;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order last update timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order assigned timestamp", example = "2024-01-15T10:35:00")
    private LocalDateTime assignedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order picked timestamp", example = "2024-01-15T10:40:00")
    private LocalDateTime pickedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order delivered timestamp", example = "2024-01-15T11:10:00")
    private LocalDateTime deliveredAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Order cancelled timestamp", example = "2024-01-15T10:32:00")
    private LocalDateTime cancelledAt;

    @Schema(description = "Delivery city", example = "Bangalore")
    private String deliveryCity;

    @Schema(description = "Restaurant city", example = "Bangalore")
    private String restaurantCity;

}