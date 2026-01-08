package com.example.ordermanagement.modules.ordering.dto;

import com.example.ordermanagement.modules.ordering.domain.OrderType;
import com.example.ordermanagement.common.domain.Address;
import com.example.ordermanagement.modules.ordering.domain.model.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Request to create a new order")
public record CreateOrderRequest(
    @NotBlank(message = "Customer ID is required")
    @Schema(description = "Unique identifier of the customer", example = "cust_12345")
    String customerId,
    
    @NotBlank(message = "Customer name is required")
    @Schema(description = "Name of the customer", example = "John Doe")
    String customerName,
    
    @NotBlank(message = "Restaurant ID is required")
    @Schema(description = "Unique identifier of the restaurant", example = "rest_67890")
    String restaurantId,
    
    @NotBlank(message = "Restaurant name is required")
    @Schema(description = "Name of the restaurant", example = "Pizza Hut")
    String restaurantName,
    
    @NotNull(message = "Order type is required")
    @Schema(description = "Type of the order", example = "FOOD")
    OrderType orderType,
    
    @NotNull(message = "Delivery address is required")
    @Valid
    @Schema(description = "Delivery address")
    Address deliveryAddress,
    
    @NotNull(message = "Restaurant address is required")
    @Valid
    @Schema(description = "Restaurant address")
    Address restaurantAddress,
    
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    @Schema(description = "List of order items")
    List<OrderItem> items,
    
    @Schema(description = "Special instructions for delivery", example = "Leave at door")
    String specialInstructions
) {}
